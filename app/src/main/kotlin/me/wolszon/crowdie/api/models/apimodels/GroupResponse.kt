package me.wolszon.crowdie.api.models.apimodels

import com.squareup.moshi.Json

data class GroupResponse(
        @Json(name = "id")
        var id: String,
        @Json(name = "members")
        var members: List<MemberResponse>
)