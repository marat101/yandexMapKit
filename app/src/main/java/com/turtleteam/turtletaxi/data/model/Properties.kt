package com.turtleteam.turtletaxi.data.model

data class Properties(
    val GeocoderMetaData: GeocoderMetaData,
    val boundedBy: List<List<Double>>,
    val description: String,
    val name: String
)