package me.wolszon.groupie.api.repository

import io.reactivex.Single
import me.wolszon.groupie.api.models.apimodels.MemberRequest
import me.wolszon.groupie.api.models.dataclass.Group
import me.wolszon.groupie.api.models.dataclass.Member

interface GroupApi {
    fun newGroup(creator: MemberRequest): Single<Group>
    fun find(id: String): Single<Group>
    fun addMember(groupId: String, member: MemberRequest): Single<Group>
    fun updateMemberRole(memberId: String, role: Int): Single<Member>
    fun sendMemberCoordsBit(memberId: String, lat: Float, lng: Float): Single<Member>
    fun kickMember(memberId: String): Single<Group>
}