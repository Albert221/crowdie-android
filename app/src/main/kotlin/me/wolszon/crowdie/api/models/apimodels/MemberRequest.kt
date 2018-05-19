package me.wolszon.crowdie.api.models.apimodels

import com.squareup.moshi.Json

class MemberRequest(
        @Json(name = "name")
        val name: String,
        @Json(name = "lat")
        val lat: Float,
        @Json(name = "lng")
        val lng: Float,
        @Json(name = "androidId")
        val androidId: String
)