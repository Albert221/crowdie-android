package me.wolszon.groupie.android.ui.group.tabs.map

import me.wolszon.groupie.api.models.dataclass.Member
import me.wolszon.groupie.base.BaseView

interface MapView : BaseView {
    fun showMembers(members: List<Member>)
}