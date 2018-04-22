package me.wolszon.groupie.api.domain

import me.wolszon.groupie.api.models.dataclass.Group
import me.wolszon.groupie.api.models.dataclass.Member

interface GroupManager : GroupClient, GroupAdmin {
    fun getState(): State?

    data class State(
            val group: Group
    ) {
        val groupId: String
            get() = group.id
        val currentUser
            get() = group.members.find { it.isYou() }!!

        fun isAdmin(): Boolean = currentUser.role == Member.ADMIN
    }
}