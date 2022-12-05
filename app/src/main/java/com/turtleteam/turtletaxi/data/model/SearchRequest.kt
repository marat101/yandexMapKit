package com.turtleteam.turtletaxi.data.model

data class SearchRequest(
    val boundedBy: List<List<Double>>,
    val request: String,
    val results: Int,
    val skip: Int
)