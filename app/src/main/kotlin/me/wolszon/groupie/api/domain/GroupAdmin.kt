package me.wolszon.groupie.api.domain

import io.reactivex.Observable
import io.reactivex.Single
import me.wolszon.groupie.api.models.dataclass.Group

interface GroupAdmin {
    fun updateRole(memberId: String, role: Int): Single<Group>
    fun kickMember(memberId: String): Single<Group>

    fun getGroupObservable(): Observable<out Group>

    class NoPermissionsException : Exception("You don't have permissions.")
}