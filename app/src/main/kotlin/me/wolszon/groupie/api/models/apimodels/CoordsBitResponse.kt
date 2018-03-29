package me.wolszon.groupie.api.models.apimodels

import com.squareup.moshi.Json

data class CoordsBitResponse(
    @Json(name = "lat")
    var lat: Double,
    @Json(name = "lng")
    var lng: Double
)