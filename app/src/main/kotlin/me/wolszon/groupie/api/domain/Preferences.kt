package me.wolszon.groupie.api.domain

interface Preferences {
    var username: String

    var lastJoinedGroup: String?
    var lastJoinedGroupToken: String?
    var lastJoinedGroupMemberId: String?
}

