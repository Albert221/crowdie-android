package me.wolszon.groupie.api.repository

import io.reactivex.Single
import me.wolszon.groupie.api.models.apimodels.CreatedResponse
import me.wolszon.groupie.api.models.apimodels.MemberRequest
import me.wolszon.groupie.api.models.apimodels.GroupResponse
import retrofit2.http.*

interface GroupRetrofitApi {
    @POST("/api/v1/group")
    fun newGroup(@Body creator: MemberRequest): Single<CreatedResponse>

    @GET("/api/v1/group/{id}")
    fun findGroup(@Path("id") id: String): Single<GroupResponse>

    @POST("/api/v1/group/{id}/member")
    fun addMember(
            @Path("id") groupId: String,
            @Body member: MemberRequest
    ): Single<CreatedResponse>

    @FormUrlEncoded
    @PATCH("/api/v1/member/{id}/role")
    fun updateMemberRole(
            @Path("id") memberId: String,
            @Field("role") role: Int
    ): Single<GroupResponse>

    @FormUrlEncoded
    @PATCH("/api/v1/member/{id}/coords-bit")
    fun sendMemberCoordsBit(
            @Path("id") memberId: String,
            @Field("lat") lat: Float,
            @Field("lng") lng: Float
    ): Single<GroupResponse>

    @DELETE("/api/v1/member/{id}")
    fun kickMember(@Path("id") memberId: String): Single<GroupResponse>
}