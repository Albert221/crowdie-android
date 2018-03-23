package me.wolszon.groupie.api.models.apimodels

import com.squareup.moshi.Json

data class MemberResponse (
    @Json(name = "id")
    var id: String,
    @Json(name = "name")
    var name: String,
    @Json(name = "role")
    var role: Int,
    @Json(name = "coords")
    var coords: CoordsResponse
)