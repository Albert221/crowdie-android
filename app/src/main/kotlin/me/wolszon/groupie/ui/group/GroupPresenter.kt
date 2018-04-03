package me.wolszon.groupie.ui.group

import me.wolszon.groupie.api.models.dataclass.Group
import me.wolszon.groupie.api.models.dataclass.Member
import me.wolszon.groupie.api.repository.GroupApi
import me.wolszon.groupie.api.state.GroupState
import me.wolszon.groupie.base.BasePresenter
import me.wolszon.groupie.base.Schedulers

class GroupPresenter(val schedulers : Schedulers, val groupApi : GroupApi) : BasePresenter<GroupView>() {
    private val groupState by lazy { GroupState }
    lateinit var group: Group

    fun loadMembers() {
        compositeObservable.add(
                groupApi.find(groupState.groupId)
                        .subscribeOn(schedulers.backgroundThread())
                        .observeOn(schedulers.mainThread())
                        .subscribe({
                            group = it
                            view?.showMembers(it.members)
                        }, { view?.showErrorDialog(it) })
        )
    }

    fun promoteMember(id: String) {
        updateMemberRole(id, Member.ADMIN)
    }

    fun suppressMember(id: String) {
        updateMemberRole(id, Member.MEMBER)
    }

    private fun updateMemberRole(id: String, role: Int) {
        compositeObservable.add(
                groupApi.updateMemberRole(id, role)
                        .subscribeOn(schedulers.backgroundThread())
                        .observeOn(schedulers.mainThread())
                        .subscribe({
                            group.members.apply {
                                dropWhile { it.id == id }
                                add(it)
                                view?.updateMember(it)
                            }
                        }, { view?.showErrorDialog(it) })
        )
    }
}