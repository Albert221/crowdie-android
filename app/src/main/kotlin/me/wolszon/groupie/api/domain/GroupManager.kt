package me.wolszon.groupie.api.domain

import me.wolszon.groupie.api.models.dataclass.Group

interface GroupManager : GroupClient, GroupAdmin {
    fun getState(): State?

    data class State(
            val group: Group
    ) {
        val groupId: String
            get() = group.id
        val currentUser
            get() = group.members.find { it.isYou() }!!
    }
}