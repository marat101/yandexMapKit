package com.turtleteam.turtletaxi.data.repository

import com.turtleteam.turtletaxi.data.api.GeoObjectsApiService
import com.turtleteam.turtletaxi.data.model.Feature
import javax.inject.Inject

class AdressRepository @Inject constructor(private val api: GeoObjectsApiService) {

    suspend fun searchAdress(text:String): List<Feature> = api.getObjects(text = text, number = 10).features

}