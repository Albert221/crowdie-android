package me.wolszon.groupie.base

interface Preferences {
    var username: String

    var lastJoinedGroup: String?
    var lastJoinedGroupToken: String?
    var lastJoinedGroupMemberId: String?
}

