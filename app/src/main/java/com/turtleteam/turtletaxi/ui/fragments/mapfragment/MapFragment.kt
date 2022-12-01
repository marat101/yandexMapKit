package com.turtleteam.turtletaxi.ui.fragments.mapfragment

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import com.turtleteam.turtletaxi.databinding.FragmentMapBinding
import com.turtleteam.turtletaxi.ui.fragments.basefragment.BaseFragment
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.runtime.ui_view.ViewProvider

class MapFragment : BaseFragment<FragmentMapBinding>() {

    private lateinit var locationManager: LocationManager
    private lateinit var userLocation: PlacemarkMapObject

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        binding.mapview.map.addInputListener(object : InputListener{
//            override fun onMapTap(p0: Map, p1: Point) {
//                TODO("Not yet implemented")
//            }
//            override fun onMapLongTap(p0: Map, p1: Point) {
//                TODO("Not yet implemented")
//            }
//
//              НА ЭТО ПОКА ЗАБЕЙ
//        })

        if (ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            // НА ЭТО ТОЖЕ ЗАБЕЙ
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
            1000 * 10,
            10F,
            locationListener())

        userLocation = binding.mapview.map.mapObjects.addPlacemark(Point(0.0, 0.0),
            ViewProvider(binding.userMarker))
    }

    override fun onPause() {
        super.onPause()
        locationManager.removeUpdates(locationListener())
    }

    override fun onStop() {
        MapKitFactory.getInstance().onStop()
        binding.mapview.onStop()
        super.onStop()
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        binding.mapview.onStart()
    }

    private fun locationListener(): LocationListener = object : LocationListener {

        override fun onProviderEnabled(provider: String) {
            if (ActivityCompat.checkSelfPermission(requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
            ) {
            }
            locationManager.getLastKnownLocation(provider)?.let { showLocation(it) }
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            super.onStatusChanged(provider, status, extras)
        }

        override fun onLocationChanged(p0: Location) {
            showLocation(p0)
        }
    }

    fun showLocation(location: Location) {
        binding.mapview.map.move(
            CameraPosition(Point(location.latitude, location.longitude), 15.5f, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 0F),
            null)
        userLocation.geometry = Point(location.latitude, location.longitude)
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentMapBinding = FragmentMapBinding.inflate(inflater, container, false)
}