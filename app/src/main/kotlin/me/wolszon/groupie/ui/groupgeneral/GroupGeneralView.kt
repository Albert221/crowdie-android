package me.wolszon.groupie.ui.groupgeneral

import me.wolszon.groupie.api.models.dataclass.Member
import me.wolszon.groupie.base.BaseView

interface GroupGeneralView : BaseView {
    fun showMembersMarkers(members : List<Member>)
}