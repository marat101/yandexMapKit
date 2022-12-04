package com.turtleteam.turtletaxi

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yandex.mapkit.geometry.Point

@SuppressLint("MissingPermission")
class LocationService(context: Context) : LocationListener {

    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    private val _currentLocation = MutableLiveData<Point>()
    val currentLocation: LiveData<Point> = _currentLocation

    init {
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
            1000 * 10,
            10F,
            this)
    }
    override fun onProviderEnabled(provider: String) {
        locationManager.getLastKnownLocation(provider)?.let { this }
    }

    override fun onLocationChanged(p0: Location) {
        _currentLocation.value = Point(p0.latitude, p0.longitude)
    }
}