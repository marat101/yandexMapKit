package com.turtleteam.turtletaxi.di

import com.turtleteam.turtletaxi.data.api.GeoObjectsApiService
import com.turtleteam.turtletaxi.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(Constants.YANDEX_SEARCH_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): GeoObjectsApiService =
        retrofit.create(GeoObjectsApiService::class.java)
}
