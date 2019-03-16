package com.workfort.weatherkit.app.ui.main

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.palette.graphics.Palette
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.workfort.weatherkit.R
import com.workfort.weatherkit.app.data.local.appconst.Const
import com.workfort.weatherkit.app.data.remote.CurrentWeatherResponse
import com.workfort.weatherkit.app.data.remote.Forecast
import com.workfort.weatherkit.app.data.remote.WeatherForecastResponse
import com.workfort.weatherkit.util.helper.CalculationUtil
import com.workfort.weatherkit.util.helper.PermissionUtil
import com.workfort.weatherkit.util.helper.Toaster
import com.workfort.weatherkit.util.lib.remote.ApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import java.io.IOException
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val disposable: CompositeDisposable = CompositeDisposable()

    private val apiService by lazy { ApiService.create() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Places.initialize(applicationContext, getString(R.string.api_key))

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val permissionUtil = PermissionUtil.on()
        if(permissionUtil.isAllowed(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            getLocation()
        }else {
            permissionUtil.request(
                this, Const.RequestCode.LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
            )
        }

        createPaletteAsync(
            BitmapFactory.decodeResource(
                resources, R.drawable.img_bg_2
            )
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == Const.RequestCode.LOCATION) {
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation()
            }
        }
    }

    private fun getLocation() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->
                if(location == null) {
                    Toaster(this).showToast(R.string.open_gps_exception)
                    return@addOnSuccessListener
                }

                getCurrentWeatherData(location.latitude, location.longitude)
                get5DayForecastData(location.latitude, location.longitude)

                val geo = Geocoder(this, Locale.getDefault())
                Thread(Runnable {
                    var addresses: List<Address> = emptyList()
                    try {
                        addresses = geo.getFromLocation(
                            location.latitude, location.longitude, 1
                        )
                    } catch (ioException: IOException) {
                        Timber.e(ioException)
                        Toaster(this).showToast(R.string.service_not_available)
                    } catch (illegalArgumentException: IllegalArgumentException) {
                        Timber.e(illegalArgumentException)
                        Toaster(this).showToast(R.string.invalid_lat_long_used)

                    }

                    if(!addresses.isNullOrEmpty()) {
                        runOnUiThread {
                            tv_city.text = addresses[0].locality
                            tv_country.text = addresses[0].countryName
                        }
                    }
                }).start()
            }
    }

//    private fun getPlaceInfo() {
//        val placesClient = Places.createClient(this)
//        val fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.PHOTO_METADATAS)
//        val placeRequest = FindCurrentPlaceRequest.builder(fields).build()
//
//        val placeResponse = placesClient.findCurrentPlace(placeRequest)
//        placeResponse.addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//                val response = task.result
//                for (placeLikelihood in response?.placeLikelihoods!!) {
//                    Timber.e(String.format(
//                        "Place '%s' has likelihood: %f",
//                        placeLikelihood.place.name,
//                        placeLikelihood.likelihood
//                    )
//                    )
//                }
//            } else {
//                val exception = task.exception
//                if (exception is ApiException) {
//                    Timber.e("Place not found: ${exception.statusCode}")
//                }
//            }
//        }
//    }

    private fun getCurrentWeatherData(lat: Double, lng: Double) {
        val params = HashMap<String, Any>()
        params["lat"] =  lat
        params["lon"] =  lng
        params["appid"] = getString(R.string.weather_api_key)
        disposable.add(apiService.getCurrentWeather(params)
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

        tv_weather.text = weather.weather[0].main

        val windSpeed = "${weather.wind.speed} mps"
        tv_wind_speed.text = windSpeed

        val humidity = "${weather.main.humidity}%"
        tv_humidity.text = humidity
    }

    private fun get5DayForecastData(lat: Double, lng: Double) {
        val params = HashMap<String, Any>()
        params["lat"] =  lat
        params["lon"] =  lng
        params["appid"] = getString(R.string.weather_api_key)
        disposable.add(apiService.get5DayWeather(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    set5DayForecastData(it)
                },{
                    Toaster(this).showToast(it.message!!)
                    Timber.e(it)
                }
            )
        )
    }

    private fun set5DayForecastData(forecast: WeatherForecastResponse) {
        var sameDay = -1
        for (i in 0..(forecast.list.size - 1)) {
            val it = forecast.list[i]
            val cal = Calendar.getInstance()
            cal.timeInMillis = it.date

            val dayNum = cal.get(Calendar.DAY_OF_WEEK)
            if(dayNum != sameDay) {
                sameDay = dayNum
                Timber.e("day $dayNum")
            }else {
                Timber.e("${it.date}")
            }
        }
    }

    private fun createPaletteAsync(bitmap: Bitmap) {
        Palette.from(bitmap).generate { palette ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.statusBarColor = palette?.getDarkVibrantColor(
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
