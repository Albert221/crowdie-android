package me.wolszon.groupie.ui.group

import me.wolszon.groupie.api.models.dataclass.Member
import me.wolszon.groupie.base.BaseView

interface GroupView : BaseView {
    fun showMembers(members : List<Member>, imperceptibly: Boolean = false)
    fun focusMemberOnMap(id: String)
    fun updateMember(member: Member)
    fun displayMemberBlockConfirmation(member: Member, callback: (Boolean) -> Unit)
    fun removeMember(member: Member)
}