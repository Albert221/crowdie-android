package me.wolszon.groupie.api.repository

import io.reactivex.Single
import me.wolszon.groupie.api.models.apimodels.GroupResponse
import me.wolszon.groupie.api.models.apimodels.MemberResponse
import retrofit2.http.*

interface GroupRetrofitApi {
    @GET("/group/{id}")
    fun findGroup(@Path("id") id: String): Single<GroupResponse>

    @FormUrlEncoded
    @PATCH("/member/{id}/role")
    fun updateMemberRole(@Path("id") memberId: String, @Field("role") role: Int): Single<MemberResponse>

    @FormUrlEncoded
    @PATCH("/member/{id}/coords-bit")
    fun sendMemberCoordsBit(@Path("id") memberId: String, @Field("lat") lat: Float, @Field("lng") lng: Float): Single<MemberResponse>
}