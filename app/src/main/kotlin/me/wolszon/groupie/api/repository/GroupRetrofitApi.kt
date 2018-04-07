package me.wolszon.groupie.api.repository

import io.reactivex.Single
import me.wolszon.groupie.api.models.apimodels.GroupResponse
import me.wolszon.groupie.api.models.apimodels.MemberResponse
import retrofit2.http.*

interface GroupRetrofitApi {
    @FormUrlEncoded
    @POST("/group")
    fun newGroup(
            @Field("name") creatorName: String,
            @Field("lat") creatorLat: Float,
            @Field("lng") creatorLng: Float
    ): Single<GroupResponse>

    @GET("/group/{id}")
    fun findGroup(@Path("id") id: String): Single<GroupResponse>

    @FormUrlEncoded
    @POST("/group/{id}/member")
    fun addMember(
            @Path("id") groupId: String,
            @Field("name") name: String,
            @Field("lat") lat: Float,
            @Field("lng") lng: Float
    ): Single<GroupResponse>

    @FormUrlEncoded
    @PATCH("/member/{id}/role")
    fun updateMemberRole(
            @Path("id") memberId: String,
            @Field("role") role: Int
    ): Single<MemberResponse>

    @FormUrlEncoded
    @PATCH("/member/{id}/coords-bit")
    fun sendMemberCoordsBit(
            @Path("id") memberId: String,
            @Field("lat") lat: Float,
            @Field("lng") lng: Float
    ): Single<MemberResponse>

    @DELETE("/member/{id}")
    fun kickMember(@Path("id") memberId: String): Single<GroupResponse>
}