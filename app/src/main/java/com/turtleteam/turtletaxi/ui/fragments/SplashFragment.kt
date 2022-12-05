package com.turtleteam.turtletaxi.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.replace
import androidx.lifecycle.lifecycleScope
import com.turtleteam.turtletaxi.R
import com.turtleteam.turtletaxi.ui.fragments.mapfragment.MapFragment
import com.yandex.mapkit.MapKitFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashFragment : Fragment(R.layout.fragment_splash) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            MapKitFactory.setApiKey("99abf47c-61c9-4962-ba47-0f4c83918410")
            delay(500)
            parentFragmentManager.beginTransaction().replace(R.id.fragmentContainerView, MapFragment()).commitNow()
        }
    }
}