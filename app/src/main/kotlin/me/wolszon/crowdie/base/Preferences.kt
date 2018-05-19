package me.wolszon.crowdie.base

interface Preferences {
    var username: String

    var lastJoinedGroup: String?
    var lastJoinedGroupToken: String?
    var lastJoinedGroupMemberId: String?
}

