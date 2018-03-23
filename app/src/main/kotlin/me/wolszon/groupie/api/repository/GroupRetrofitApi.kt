package me.wolszon.groupie.api.repository

import io.reactivex.Single
import me.wolszon.groupie.api.models.apimodels.GroupResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface GroupRetrofitApi {
    @GET("/group/{id}")
    fun findGroup(@Path("id") id : String) : Single<GroupResponse>
}