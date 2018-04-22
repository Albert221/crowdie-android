package me.wolszon.groupie.api.domain

import io.reactivex.Single
import me.wolszon.groupie.api.models.dataclass.Group

interface GroupAdmin {
    fun updateRole(memberId: String, role: Int): Single<Group>
    fun kickMember(memberId: String): Single<Group>

    class NoPermissionsException : Exception("You don't have permissions.")
}