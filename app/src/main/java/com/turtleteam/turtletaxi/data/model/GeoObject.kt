package com.turtleteam.turtletaxi.data.model

data class GeoObject(
    val features: List<Feature>,
    val properties: PropertiesX,
    val type: String
)