package me.wolszon.groupie.api.repository

import io.reactivex.Single
import me.wolszon.groupie.api.models.dataclass.Group
import me.wolszon.groupie.api.models.dataclass.Member

interface GroupApi {
    fun newGroup(creatorName: String, creatorLat: Float, creatorLng: Float): Single<Group>
    fun find(id: String): Single<Group>
    fun addMember(groupId: String, name: String, lat: Float, lng: Float): Single<Group>
    fun updateMemberRole(memberId: String, role: Int): Single<Member>
    fun sendMemberCoordsBit(memberId: String, lat: Float, lng: Float): Single<Member>
    fun kickMember(memberId: String): Single<Group>
}