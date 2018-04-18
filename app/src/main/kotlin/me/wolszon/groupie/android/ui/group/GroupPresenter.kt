package me.wolszon.groupie.android.ui.group

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import me.wolszon.groupie.android.ui.Navigator
import me.wolszon.groupie.api.ApiGroupManager
import me.wolszon.groupie.api.GroupManager
import me.wolszon.groupie.api.models.dataclass.Group
import me.wolszon.groupie.api.models.dataclass.Member
import me.wolszon.groupie.base.BasePresenter
import me.wolszon.groupie.base.Schedulers
import java.util.concurrent.TimeUnit

class GroupPresenter(private val groupManager: GroupManager,
                     private val navigator: Navigator,
                     private val schedulers: Schedulers) : BasePresenter<GroupView>() {
    override fun subscribe(view: GroupView) {
        super.subscribe(view)

        run {
            groupManager
                    .getGroupObservable()
                    .subscribeOn(schedulers.backgroundThread())
                    .observeOn(schedulers.mainThread())
                    .subscribe({ view.showMembers(it.members) }, { view.showErrorDialog(it) })
        }

        run {
            Observable.interval(3, 2, TimeUnit.SECONDS)
                    .subscribe { run { groupManager.update().process() } }
        }
    }

    fun loadMembers() = run { groupManager.update().process() }

    fun promoteMember(id: String) = run { groupManager.updateRole(id, Member.ADMIN).process() }

    fun suppressMember(id: String) = run { groupManager.updateRole(id, Member.MEMBER).process() }

    fun blockMember(id: String) {
        groupManager as ApiGroupManager

        val member = groupManager.state!!.group.members.find { it.id == id }!!
        view?.displayMemberBlockConfirmation(member) {
            result ->
            if (!result) return@displayMemberBlockConfirmation

            run {
                groupManager.kickMember(id).process()
            }
        }
    }

    fun leaveGroup() {
        run {
            groupManager.leaveGroup().process()
        }
        navigator.openLandingActivity()
    }

    fun showQr() {
        groupManager as ApiGroupManager
        navigator.openGroupQrActivity(groupManager.state!!.groupId)
    }

    private fun Single<Group>.process(): Disposable {
        return this
                .subscribeOn(schedulers.backgroundThread())
                .observeOn(schedulers.mainThread())
                .subscribe({}, { view?.showErrorDialog(it) })
    }
}