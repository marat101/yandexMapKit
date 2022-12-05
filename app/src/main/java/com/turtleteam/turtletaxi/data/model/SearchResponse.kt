package com.turtleteam.turtletaxi.data.model

data class SearchResponse(
    val boundedBy: List<List<Double>>,
    val display: String,
    val found: Int
)