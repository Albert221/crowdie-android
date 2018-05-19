package me.wolszon.crowdie.api.models.apimodels

import com.squareup.moshi.Json

data class CoordsBitResponse(
    @Json(name = "lat")
    var lat: Float,
    @Json(name = "lng")
    var lng: Float
)