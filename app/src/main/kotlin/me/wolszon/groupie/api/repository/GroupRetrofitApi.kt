package me.wolszon.groupie.api.repository

import io.reactivex.Single
import me.wolszon.groupie.api.models.apimodels.MemberRequest
import me.wolszon.groupie.api.models.apimodels.GroupResponse
import me.wolszon.groupie.api.models.apimodels.MemberResponse
import retrofit2.http.*

interface GroupRetrofitApi {
    @POST("/group")
    fun newGroup(@Body creator: MemberRequest): Single<GroupResponse>

    @GET("/group/{id}")
    fun findGroup(@Path("id") id: String): Single<GroupResponse>

    @POST("/group/{id}/member")
    fun addMember(
            @Path("id") groupId: String,
            @Body member: MemberRequest
    ): Single<GroupResponse>

    @FormUrlEncoded
    @PATCH("/member/{id}/role")
    fun updateMemberRole(
            @Path("id") memberId: String,
            @Field("role") role: Int
    ): Single<GroupResponse>

    @FormUrlEncoded
    @PATCH("/member/{id}/coords-bit")
    fun sendMemberCoordsBit(
            @Path("id") memberId: String,
            @Field("lat") lat: Float,
            @Field("lng") lng: Float
    ): Single<GroupResponse>

    @DELETE("/member/{id}")
    fun kickMember(@Path("id") memberId: String): Single<GroupResponse>
}