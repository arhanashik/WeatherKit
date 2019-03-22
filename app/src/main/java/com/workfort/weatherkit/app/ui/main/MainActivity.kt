package com.workfort.weatherkit.app.ui.main

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.text.format.DateFormat
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPhotoRequest
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.workfort.weatherkit.R
import com.workfort.weatherkit.app.data.local.appconst.Constant
import com.workfort.weatherkit.app.data.local.pref.PrefProp
import com.workfort.weatherkit.app.data.local.pref.PrefUtil
import com.workfort.weatherkit.app.data.remote.CurrentWeatherResponse
import com.workfort.weatherkit.app.data.remote.WeatherForecastResponse
import com.workfort.weatherkit.app.ui.main.adapter.ForecastAdapter
import com.workfort.weatherkit.util.helper.*
import com.workfort.weatherkit.util.lib.remote.PlaceApiService
import com.workfort.weatherkit.util.lib.remote.WeatherApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import java.io.IOException
import java.lang.StringBuilder
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.ln

class MainActivity : AppCompatActivity() {

    //private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val disposable: CompositeDisposable = CompositeDisposable()

    private val placeApiService by lazy { PlaceApiService.create() }
    private val weatherApiService by lazy { WeatherApiService.create() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Places.initialize(applicationContext, getString(R.string.google_api_key))

        //fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val permissionUtil = PermissionUtil.on()
        if(permissionUtil.isAllowed(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            getPlaceInfo()
        }else {
            permissionUtil.request(
                this, Constant.RequestCode.LOCATION, Manifest.permission.ACCESS_FINE_LOCATION
            )
        }

        createPaletteAsync(
            BitmapFactory.decodeResource(
                resources, R.drawable.img_bg
            )
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == Constant.RequestCode.LOCATION) {
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getPlaceInfo()
            }
        }
    }

//    private fun getLocation() {
//        fusedLocationClient.lastLocation
//            .addOnSuccessListener { location : Location? ->
//                if(location == null) {
//                    Toaster(this).showToast(R.string.open_gps_exception)
//                    return@addOnSuccessListener
//                }
//
//                setCurrentLocationInfo(location.latitude, location.longitude)
//                getCurrentWeatherData(location.latitude, location.longitude)
//                get5DayForecastData(location.latitude, location.longitude)
//            }
//    }

    private fun getPlaceInfo() {
        val placesClient = Places.createClient(this)
        val fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.PHOTO_METADATAS)
        val placeRequest = FindCurrentPlaceRequest.builder(fields).build()

        FetchPlaceRequest.builder("", fields).build()
        val placeResponse = placesClient.findCurrentPlace(placeRequest)
        placeResponse.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val response = task.result
                for (placeLikelihood in response?.placeLikelihoods!!) {
                    Timber.e(String.format(
                        "Place '%s' has likelihood: %f", placeLikelihood.place.name, placeLikelihood.likelihood
                    ))
                }
                val location = response.placeLikelihoods[0].place.latLng

                PrefUtil.set(PrefProp.LAT, location?.latitude!!.toString())
                PrefUtil.set(PrefProp.LNG, location.longitude.toString())

                setCurrentLocationInfo(location.latitude, location.longitude)
                getCurrentWeatherData(location.latitude, location.longitude)
                get5DayForecastData(location.latitude, location.longitude)
            } else {
                val exception = task.exception
                if (exception is ApiException) {
                    Timber.e("Place not found: ${exception.message}")
                }
                val lat = PrefUtil.get(PrefProp.LAT, "0")?.toDouble()
                val lng = PrefUtil.get(PrefProp.LNG, "0")?.toDouble()
                setCurrentLocationInfo(lat!!, lng!!)
                getCurrentWeatherData(lat.toDouble(), lng.toDouble())
                get5DayForecastData(lat.toDouble(), lng.toDouble())
            }
        }
    }

    private fun setCurrentLocationInfo(lat: Double, lng: Double) {
        val geo = Geocoder(this, Locale.getDefault())
        Thread(Runnable {
            var addresses: List<Address> = emptyList()
            try {
                addresses = geo.getFromLocation(lat, lng, 1)
            } catch (ioException: IOException) {
                Timber.e(ioException)
                runOnUiThread { Toaster(this).showToast(R.string.service_not_available) }
            } catch (illegalArgumentException: IllegalArgumentException) {
                Timber.e(illegalArgumentException)
                runOnUiThread { Toaster(this).showToast(R.string.invalid_lat_long_used) }
            }

            if(!addresses.isNullOrEmpty()) {
                runOnUiThread {
                    tv_city.text = addresses[0].locality
                    tv_country.text = addresses[0].countryName
                    getCityInfo(addresses[0].locality)
                }
            }
        }).start()
    }

    private fun getCityInfo(city: String) {

        val params = HashMap<String, Any>()
        params["input"] = city
        params["inputtype"] = "textquery"
        params["fields"] = "place_id,name"
        params["key"] = getString(R.string.google_web_api_key)

        disposable.add(placeApiService.findPlace(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { placeResponse ->
                    if(placeResponse.status == "OK") {
                        if(!placeResponse.candidates.isNullOrEmpty()) {
                            getPlacePhoto(placeResponse.candidates[0].placeId)
                        }
                    }
                }, { exception ->
                    Timber.e(exception)
                }
            )
        )
    }

    private fun getPlacePhoto(placeId: String) {
        val fields = Arrays.asList(Place.Field.PHOTO_METADATAS)
        val placeRequest = FetchPlaceRequest.builder(placeId, fields).build()

        val placeClient = Places.createClient(this)
        placeClient.fetchPlace(placeRequest).addOnSuccessListener { placeResponse ->
            val photoMetadata = placeResponse.place.photoMetadatas?.get(0) ?: return@addOnSuccessListener

            val attributions = photoMetadata.attributions
            Timber.e("attributions: $attributions")

            val displayParams = AndroidUtil().getDisplayParams(this)
            val photoRequest = FetchPhotoRequest.builder(photoMetadata)
                .setMaxWidth(displayParams.width)
                .setMaxHeight(displayParams.height)
                .build()

            placeClient.fetchPhoto(photoRequest).addOnSuccessListener { photoResponse ->
                val bitmap = photoResponse.bitmap
                img_bg.setImageBitmap(bitmap)
                createPaletteAsync(bitmap)
            }.addOnFailureListener { exception ->
                if (exception is ApiException) {
                    val statusCode = exception.statusCode
                    Timber.e("Place not found: ${exception.message}")
                }
            }
        }
    }

    private fun getCurrentWeatherData(lat: Double, lng: Double) {
        val params = HashMap<String, Any>()
        params["lat"] =  lat
        params["lon"] =  lng
        params["appid"] = getString(R.string.weather_api_key)
        disposable.add(weatherApiService.getCurrentWeather(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    setCurrentWeatherData(it)
                },{
                    Toaster(this).showToast(it.message!!)
                    Timber.e(it)
                }
            )
        )
    }

    private fun setCurrentWeatherData(weather: CurrentWeatherResponse) {
        val calc = CalculationUtil()

        val temp = calc.toCelsius(weather.main.temp).toInt()
        tv_temperature.text = temp.toString()

        val icon = WeatherKitUtil().getWeatherIcon(weather.weather[0].main.toLowerCase())
        tv_weather.text = weather.weather[0].main
        tv_weather.setCompoundDrawablesWithIntrinsicBounds(icon,  0, 0, 0)

        val windSpeed = "${weather.wind.speed} mps"
        tv_wind_speed.text = windSpeed

        val humidity = "${weather.main.humidity}%"
        tv_humidity.text = humidity

        val cal1 = Calendar.getInstance()
        cal1.timeInMillis = weather.common.sunrise
        val sunrise = DateFormat.format("HH:mm", cal1.time)
        tv_sunrise.text = sunrise

        val cal2 = Calendar.getInstance()
        cal2.timeInMillis = weather.common.sunset
        val sunset = DateFormat.format("HH:mm", cal2.time)
        tv_sunset.text = sunset

        if(sunrise != sunset) {
            tv_sunrise.visibility = View.VISIBLE
            tv_sunset.visibility = View.VISIBLE
        }
    }

    private fun get5DayForecastData(lat: Double, lng: Double) {
        val adapter = ForecastAdapter()
        rv_forecast.setHasFixedSize(true)
        rv_forecast.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        rv_forecast.adapter = adapter

        val params = HashMap<String, Any>()
        params["lat"] =  lat
        params["lon"] =  lng
        params["appid"] = getString(R.string.weather_api_key)
        Timber.e("$lat : $lng")
        disposable.add(weatherApiService.get5DayWeather(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    pb.visibility = View.INVISIBLE
                    if(!it.list.isNullOrEmpty()) {
                        rv_forecast.visibility = View.VISIBLE
                        adapter.setForecastList(it.list)
                    }else {
                        tv_no_forecast.visibility = View.VISIBLE
                    }
                },{
                    pb.visibility = View.INVISIBLE
                    tv_no_forecast.visibility = View.VISIBLE
                    Toaster(this).showToast(it.message!!)
                    Timber.e(it)
                }
            )
        )
    }

    private fun createPaletteAsync(bitmap: Bitmap) {
        Palette.from(bitmap).generate { palette ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.statusBarColor = palette?.getDominantColor(
                    ContextCompat.getColor(this, R.color.colorPrimaryDark)
                )!!
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }
}
