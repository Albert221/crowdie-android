package me.wolszon.groupie.ui.group

import me.wolszon.groupie.api.models.dataclass.Group
import me.wolszon.groupie.api.models.dataclass.Member
import me.wolszon.groupie.api.repository.GroupApi
import me.wolszon.groupie.api.state.GroupState
import me.wolszon.groupie.base.BasePresenter
import me.wolszon.groupie.base.Schedulers
import me.wolszon.groupie.ui.Navigator

class GroupPresenter(val schedulers: Schedulers, val groupApi: GroupApi, val navigator: Navigator) : BasePresenter<GroupView>() {
    private val groupState by lazy { GroupState }
    lateinit var group: Group

    // This method is called so frequently, because during
    fun loadMembers() {
        run {
            groupApi.find(groupState.groupId)
                    .subscribeOn(schedulers.backgroundThread())
                    .observeOn(schedulers.mainThread())
                    .subscribe({
                        group = it
                        view?.showMembers(it.members)
                    }, { view?.showErrorDialog(it) })
        }
    }

    fun promoteMember(id: String) {
        updateMemberRole(id, Member.ADMIN)
    }

    fun suppressMember(id: String) {
        updateMemberRole(id, Member.MEMBER)
    }

    private fun updateMemberRole(id: String, role: Int) {
        run {
            groupApi.updateMemberRole(id, role)
                    .subscribeOn(schedulers.backgroundThread())
                    .observeOn(schedulers.mainThread())
                    .subscribe({
                        loadMembers()
                    }, { view?.showErrorDialog(it) })
        }
    }

    fun blockMember(id: String) {
        val member = group.members.find { it.id == id }!!

        view?.displayMemberBlockConfirmation(member) {
            result ->
            if (!result) return@displayMemberBlockConfirmation

            run {
                groupApi.kickMember(id)
                        .subscribeOn(schedulers.backgroundThread())
                        .observeOn(schedulers.mainThread())
                        .subscribe({
                            loadMembers()
                        }, { view?.showErrorDialog(it) })
            }
        }
    }

    fun showQr() {
        navigator.openGroupQrActivity(group.id)
    }
}