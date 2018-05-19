package me.wolszon.crowdie.android.ui.group.tabs.members

import me.wolszon.crowdie.api.models.dataclass.Member
import me.wolszon.crowdie.base.BaseView

interface MembersView : BaseView {
    fun showMembers(members: List<Member>)
    fun displayMemberBlockConfirmation(member: Member, callback: (Boolean) -> Unit)
    fun openLandingActivity()
}