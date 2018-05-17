package me.wolszon.groupie.android.ui.group.tabs.members

import me.wolszon.groupie.api.models.dataclass.Member
import me.wolszon.groupie.base.BaseView

interface MembersView : BaseView {
    fun showMembers(members: List<Member>)
    fun displayMemberBlockConfirmation(member: Member, callback: (Boolean) -> Unit)
}