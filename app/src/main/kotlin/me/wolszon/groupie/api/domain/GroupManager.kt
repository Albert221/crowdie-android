package me.wolszon.groupie.api.domain

import io.reactivex.Observable
import io.reactivex.Single
import me.wolszon.groupie.api.models.dataclass.Group
import me.wolszon.groupie.api.models.dataclass.Member

interface GroupManager {
    companion object {
         var state: State? = null
    }

    // Client methods
    fun getGroupObservable(): Observable<out StateFeed>

    fun newGroup(): Single<Group>
    fun joinGroup(groupId: String): Single<Group>
    fun sendCoords(lat: Float, lng: Float): Single<Group>
    fun update(): Single<Group>
    fun leaveGroup(): Single<Group>

    // Admin methods
    fun updateRole(memberId: String, role: Int): Single<Group>
    fun kickMember(memberId: String): Single<Group>

    class NoPermissionsException : Exception("You don't have permissions.")

    data class State(
            var group: Group,
            var token: String,
            var currentMemberId: String
    ) {
        fun getGroupId(): String = group.id

        fun getCurrentUser(): Member = group.members.find { it.isYou() }!!

        fun isAdmin(): Boolean = getCurrentUser().role == Member.ADMIN
    }
}