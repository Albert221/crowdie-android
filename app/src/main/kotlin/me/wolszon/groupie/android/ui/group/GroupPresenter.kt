package me.wolszon.groupie.android.ui.group

import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import me.wolszon.groupie.android.ui.Navigator
import me.wolszon.groupie.api.ApiGroupManager
import me.wolszon.groupie.api.GroupManager
import me.wolszon.groupie.api.models.dataclass.Group
import me.wolszon.groupie.api.models.dataclass.Member
import me.wolszon.groupie.base.BasePresenter
import java.util.concurrent.TimeUnit

class GroupPresenter(private val groupManager: GroupManager,
                     private val navigator: Navigator) : BasePresenter<GroupView>() {
    override fun subscribe(view: GroupView) {
        super.subscribe(view)

        groupManager.subscribe(object : Observer<Group> {
            override fun onComplete() = Unit
            override fun onSubscribe(d: Disposable) = Unit
            override fun onNext(t: Group) = view.showMembers(t.members)
            override fun onError(e: Throwable) = view.showErrorDialog(e)
        })

        Observable.interval(3, 2, TimeUnit.SECONDS)
                .subscribe { groupManager.update() }
    }

    fun loadMembers() = groupManager.update()

    fun promoteMember(id: String) = groupManager.updateRole(id, Member.ADMIN)

    fun suppressMember(id: String) = groupManager.updateRole(id, Member.MEMBER)

    fun blockMember(id: String) {
        groupManager as ApiGroupManager

        val member = groupManager.state!!.group.members.find { it.id == id }!!
        view?.displayMemberBlockConfirmation(member) {
            result ->
            if (!result) return@displayMemberBlockConfirmation

            groupManager.kickMember(id)
        }
    }

    fun leaveGroup() {
        groupManager.leaveGroup()
        navigator.openLandingActivity()
    }

    fun showQr() {
        groupManager as ApiGroupManager
        navigator.openGroupQrActivity(groupManager.state!!.groupId)
    }
}