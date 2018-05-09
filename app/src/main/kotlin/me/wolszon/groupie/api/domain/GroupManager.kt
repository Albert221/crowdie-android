package me.wolszon.groupie.api.domain

import me.wolszon.groupie.api.models.dataclass.Group
import me.wolszon.groupie.api.models.dataclass.Member

interface GroupManager : GroupClient, GroupAdmin {
    companion object {
         var state: State? = null
    }

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