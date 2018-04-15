package me.wolszon.groupie.api.state

import me.wolszon.groupie.api.models.dataclass.Member

object GroupState {
    var groupId: String? = null
    var currentUser: Member? = null

    fun boot(groupId: String, currentUser: Member) {
        this.groupId = groupId
        this.currentUser = currentUser
    }

    fun destroy() {
        this.groupId = null
        this.currentUser = null
    }
}