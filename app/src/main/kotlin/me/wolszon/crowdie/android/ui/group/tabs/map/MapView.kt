package me.wolszon.crowdie.android.ui.group.tabs.map

import me.wolszon.crowdie.api.models.dataclass.Member
import me.wolszon.crowdie.base.BaseView

interface MapView : BaseView {
    fun showMembers(members: List<Member>)
    fun focusMemberOnMap(id: String)
}