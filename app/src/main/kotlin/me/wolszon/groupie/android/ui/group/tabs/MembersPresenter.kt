package me.wolszon.groupie.android.ui.group.tabs

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import me.wolszon.groupie.api.domain.GroupManager
import me.wolszon.groupie.api.domain.StateFeed
import me.wolszon.groupie.api.models.dataclass.Group
import me.wolszon.groupie.api.models.dataclass.Member
import me.wolszon.groupie.base.BasePresenter
import me.wolszon.groupie.base.Schedulers

class MembersPresenter(private val groupManager: GroupManager,
                       private val schedulers: Schedulers) : BasePresenter<MembersTab>() {
    override fun subscribe(view: MembersTab) {
        super.subscribe(view)

        run {
            groupManager
                    .getGroupObservable()
                    .subscribeOn(schedulers.backgroundThread())
                    .observeOn(schedulers.mainThread())
                    .subscribe {
                        when (it.event) {
                            StateFeed.Event.UPDATE -> {
                                handleUpdate(it.updatedGroup!!)
                            }

                            else -> Unit
                        }
                    }
        }
    }

    private fun handleUpdate(group: Group) {
        view?.showMembers(group.members)
    }

    fun promoteMember(id: String) = run { groupManager.updateRole(id, Member.ADMIN).process() }

    fun suppressMember(id: String) = run { groupManager.updateRole(id, Member.MEMBER).process() }

    fun blockMember(id: String) {
        val member = GroupManager.state!!.group.members.find { it.id == id }!!
        view?.displayMemberBlockConfirmation(member) {
            result ->
            if (!result) return@displayMemberBlockConfirmation

            run {
                groupManager.kickMember(id).process()
            }
        }
    }

    private fun Single<Group>.process(): Disposable {
        return this
                .subscribeOn(schedulers.backgroundThread())
                .observeOn(schedulers.mainThread())
                .subscribe({}, { view?.showErrorDialog(it) })
    }
}