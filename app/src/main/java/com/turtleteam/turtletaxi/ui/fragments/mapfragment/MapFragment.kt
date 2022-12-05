package com.turtleteam.turtletaxi.ui.fragments.mapfragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.turtleteam.turtletaxi.LocationService
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

class MapFragment : BaseFragment<FragmentMapBinding>(), GeoObjectTapListener,
    InputListener {

    private lateinit var userLocation: PlacemarkMapObject
    private lateinit var endPoint: PlacemarkMapObject
    private lateinit var locService: LocationService
    private var isShowed = false

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.initialize(this.context)
        locService = LocationService(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        endPoint = binding.mapview.map.mapObjects.addPlacemark(Point(0.0, 0.0),
            ViewProvider(binding.finishMarker))

        userLocation = binding.mapview.map.mapObjects.addPlacemark(Point(0.0, 0.0),
            ViewProvider(binding.userMarker))

        addClickListeners()
        observableData()
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
        p0.geoObject.geometry.forEach{Log.e("aaaaaaa", "${it.point?.latitude} ${it.point?.longitude}")}


        binding.mapview.map.move(
            CameraPosition(p0.geoObject.geometry.random().point!!, 16.5f, 0.0f, 0.0f),
            Animation(Animation.Type.LINEAR, 0.5F),
            null)

        binding.mapview.map.selectGeoObject(selectionMetadata.id,
            selectionMetadata.layerId)

        return true
    }

    override fun onPause() {
        super.onPause()
        locService.locationManager.removeUpdates(locService)
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

    private fun observableData() {
        locService.currentLocation.observe(viewLifecycleOwner) {
            if (!isShowed) {
                binding.mapview.map.move(
                    CameraPosition(it, 16.5f, 0.0f, 0.0f),
                    Animation(Animation.Type.LINEAR, 0.5F),
                    null)
                isShowed = true
            }
            userLocation.geometry = it
        }
    }

    private fun addClickListeners() {
        binding.moveToUser.setOnClickListener {
            if (locService.currentLocation.value != null) {
                binding.mapview.map.move(
                    CameraPosition(locService.currentLocation.value!!, 16.5f, 0.0f, 0.0f),
                    Animation(Animation.Type.LINEAR, 0.5F),
                    null)
                userLocation.geometry = locService.currentLocation.value!!
            }
        }
        binding.mapview.map.apply {
            addTapListener(this@MapFragment)
            addInputListener(this@MapFragment)
        }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentMapBinding = FragmentMapBinding.inflate(inflater, container, false)
}