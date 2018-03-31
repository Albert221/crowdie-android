package me.wolszon.groupie.ui.group

import me.wolszon.groupie.api.models.dataclass.Member
import me.wolszon.groupie.base.BaseView

interface GroupView : BaseView {
    fun showMembers(members : List<Member>, focus: Boolean = true)
    fun focusMemberOnMap(id: String)
}