package com.turtleteam.turtletaxi.ui.fragments.mapfragment

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import com.turtleteam.turtletaxi.databinding.FragmentMapBinding
import com.turtleteam.turtletaxi.ui.fragments.basefragment.BaseFragment
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.layers.GeoObjectTapListener
import com.yandex.mapkit.map.*
import com.yandex.mapkit.map.Map
import com.yandex.runtime.ui_view.ViewProvider


class MapFragment : BaseFragment<FragmentMapBinding>() {

    private lateinit var locationManager: LocationManager
    private lateinit var userLocation: PlacemarkMapObject
    private lateinit var endPoint: PlacemarkMapObject
    private lateinit var inputListener: InputListener
    private lateinit var objectTapListener: GeoObjectTapListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        locationManager =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        inputListener = gerInputListener()
        objectTapListener = getObjectTapListener()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userLocation = binding.mapview.map.mapObjects.addPlacemark(Point(0.0, 0.0),
            ViewProvider(binding.userMarker))
        endPoint = binding.mapview.map.mapObjects.addPlacemark(Point(0.0, 0.0),
            ViewProvider(binding.finishMarker))

        binding.mapview.map.addTapListener(objectTapListener)
        binding.mapview.map.addInputListener(inputListener)

        if (ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
            1000 * 10,
            10F,
            locationListener())

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

    private fun gerInputListener(): InputListener = object : InputListener {
        override fun onMapTap(p0: Map, p1: Point) {
            binding.mapview.map.deselectGeoObject()
            Log.e("ssss", p1.toString())
            endPoint.geometry = p1
        }

        override fun onMapLongTap(p0: Map, p1: Point) {
        }
    }

    private fun getObjectTapListener(): GeoObjectTapListener =
        GeoObjectTapListener { p0 ->
            val selectionMetadata: GeoObjectSelectionMetadata = p0
                .geoObject
                .metadataContainer
                .getItem(GeoObjectSelectionMetadata::class.java)

            binding.mapview.map.selectGeoObject(selectionMetadata.id,
                selectionMetadata.layerId)

            true
        }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentMapBinding = FragmentMapBinding.inflate(inflater, container, false)
}