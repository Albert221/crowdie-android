package me.wolszon.groupie.api.repository

import io.reactivex.Single
import me.wolszon.groupie.api.mapper.GroupMapper
import me.wolszon.groupie.api.mapper.MemberMapper
import me.wolszon.groupie.api.models.dataclass.Group
import me.wolszon.groupie.api.models.dataclass.Member
import retrofit2.Retrofit

class GroupRepository(val retrofit: Retrofit) : GroupApi {
    private val groupApi by lazy { retrofit.create(GroupRetrofitApi::class.java) }

    override fun find(id: String): Single<Group> =
            groupApi.findGroup(id).map { GroupMapper.map(it) }

    override fun updateMemberRole(memberId: String, role: Int): Single<Member> =
            groupApi.updateMemberRole(memberId, role).map { MemberMapper.map(it) }

    override fun sendMemberCoordsBit(memberId: String, lat: Float, lng: Float): Single<Member> =
            groupApi.sendMemberCoordsBit(memberId, lat, lng).map { MemberMapper.map(it) }
}