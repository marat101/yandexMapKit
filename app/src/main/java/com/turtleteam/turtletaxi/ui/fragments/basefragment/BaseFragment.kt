package com.turtleteam.turtletaxi.ui.fragments.basefragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.yandex.mapkit.MapKitFactory

abstract class BaseFragment<VB: ViewBinding>: Fragment() {

    lateinit var binding: VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = getViewBinding(inflater, container)
        MapKitFactory.initialize(context)

        return binding.root
    }

    abstract fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB

}