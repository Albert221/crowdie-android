package me.wolszon.crowdie.android.ui.group.tabs.members

import io.reactivex.Single
import io.reactivex.disposables.Disposable
import me.wolszon.crowdie.api.domain.GroupManager
import me.wolszon.crowdie.api.domain.StateFeed
import me.wolszon.crowdie.api.models.dataclass.Group
import me.wolszon.crowdie.api.models.dataclass.Member
import me.wolszon.crowdie.base.BasePresenter
import me.wolszon.crowdie.base.Schedulers

class MembersPresenter(private val groupManager: GroupManager,
                       private val schedulers: Schedulers) : BasePresenter<MembersView>() {
    override fun subscribe(view: MembersView) {
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