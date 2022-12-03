package com.turtleteam.turtletaxi.ui.fragments.mapfragment

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.turtleteam.turtletaxi.databinding.FragmentMapBinding
import com.turtleteam.turtletaxi.ui.fragments.basefragment.BaseFragment
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.layers.GeoObjectTapEvent
import com.yandex.mapkit.layers.GeoObjectTapListener
import com.yandex.mapkit.map.*
import com.yandex.mapkit.map.Map
import com.yandex.runtime.ui_view.ViewProvider


class MapFragment : BaseFragment<FragmentMapBinding>(), LocationListener, GeoObjectTapListener,
    InputListener {

    private lateinit var userLocation: PlacemarkMapObject
    private lateinit var endPoint: PlacemarkMapObject
    private lateinit var locationManager: LocationManager

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        locationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
            1000 * 10,
            10F,
            this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        endPoint = binding.mapview.map.mapObjects.addPlacemark(Point(0.0, 0.0),
            ViewProvider(binding.finishMarker))

        userLocation = binding.mapview.map.mapObjects.addPlacemark(Point(0.0, 0.0),
            ViewProvider(binding.userMarker))

        binding.mapview.map.addTapListener(this)
        binding.mapview.map.addInputListener(this)
    }

    @SuppressLint("MissingPermission")
    override fun onProviderEnabled(provider: String) {
        locationManager.getLastKnownLocation(provider)?.let { this }
    }

    override fun onLocationChanged(p0: Location) {
        userLocation.geometry = Point(p0.latitude, p0.longitude)
        binding.mapview.map.move(
            CameraPosition(userLocation.geometry, 15.5f, 0.0f, 0.0f),
            Animation(Animation.Type.LINEAR, 0.5F),
            null)
    }

    override fun onMapTap(p0: Map, p1: Point) {
        binding.mapview.map.deselectGeoObject()
        endPoint.geometry = Point(p1.latitude, p1.longitude)
    }

    override fun onMapLongTap(p0: Map, p1: Point) {
    }

    override fun onObjectTap(p0: GeoObjectTapEvent): Boolean {
        val selectionMetadata: GeoObjectSelectionMetadata = p0
            .geoObject
            .metadataContainer
            .getItem(GeoObjectSelectionMetadata::class.java)

        binding.mapview.map.selectGeoObject(selectionMetadata.id,
            selectionMetadata.layerId)

        return true
    }

    override fun onPause() {
        super.onPause()
        locationManager.removeUpdates(this)
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

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentMapBinding = FragmentMapBinding.inflate(inflater, container, false)
}