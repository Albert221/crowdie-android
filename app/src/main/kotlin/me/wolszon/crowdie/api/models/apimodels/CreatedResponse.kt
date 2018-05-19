package me.wolszon.crowdie.api.models.apimodels

import com.squareup.moshi.Json

data class CreatedResponse(
        @Json(name = "group")
        var group: GroupResponse,
        @Json(name = "yourId")
        var yourId: String,
        @Json(name = "token")
        var token: String
)