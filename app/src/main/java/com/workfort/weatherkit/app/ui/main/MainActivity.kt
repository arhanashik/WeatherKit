package com.workfort.weatherkit.app.ui.main

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.workfort.weatherkit.R
import com.workfort.weatherkit.app.data.local.appconst.Const
import com.workfort.weatherkit.app.data.remote.CurrentWeatherResponse
import com.workfort.weatherkit.util.helper.CalculationUtil
import com.workfort.weatherkit.util.helper.PermissionUtil
import com.workfort.weatherkit.util.helper.Toaster
import com.workfort.weatherkit.util.lib.remote.ApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import java.util.*

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
                // Got last known location. In some rare situations this can be null.
                val geo = Geocoder(this, Locale.getDefault())
                val addresses = geo.getFromLocation(
                    location?.latitude!!, location.longitude, 1
                )

                tv_city.text = addresses[0].locality

                getCurrentWeather(location.latitude, location.longitude)
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

    private fun getCurrentWeather(lat: Double, lng: Double) {
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

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }
}
