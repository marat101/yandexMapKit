package com.turtleteam.turtletaxi.data.api

import com.turtleteam.turtletaxi.data.model.GeoObject
import com.turtleteam.turtletaxi.utils.Constants
import retrofit2.http.GET
import retrofit2.http.Query

interface GeoObjectsApiService {

    @GET
    suspend fun getObjects(
        @Query("apikey") apikey: String = Constants.YANDEX_SEARCH_URL,
        @Query("text") text: String,
        @Query("lang") lang: String = Constants.LANGUAGE,
        @Query("results") number: Int
    ): GeoObject
}