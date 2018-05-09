package me.wolszon.groupie.android.ui.group

import android.util.Log
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import me.wolszon.groupie.android.ui.Navigator
import me.wolszon.groupie.api.domain.GroupManager
import me.wolszon.groupie.api.domain.StateFeed
import me.wolszon.groupie.api.models.dataclass.Group
import me.wolszon.groupie.api.models.dataclass.Member
import me.wolszon.groupie.base.BasePresenter
import me.wolszon.groupie.base.Schedulers
import java.util.concurrent.TimeUnit

class GroupPresenter(private val groupManager: GroupManager,
                     private val navigator: Navigator,
                     private val schedulers: Schedulers) : BasePresenter<GroupView>() {
    companion object {
        val TAG = GroupPresenter::class.java.simpleName!!
    }

    override fun subscribe(view: GroupView) {
        super.subscribe(view)

        run {
            groupManager
                    .getGroupObservable()
                    .subscribeOn(schedulers.backgroundThread())
                    .observeOn(schedulers.mainThread())
                    .subscribe {
                        when (it.event) {
                            StateFeed.Event.UPDATE -> {
                                view.showMembers(it.updatedGroup!!.members)
                            }

                            StateFeed.Event.KICK -> {
                                Log.i(TAG, "User has probably been kicked.")

                                view.informAboutBeingKicked()
                                navigator.openLandingActivity()
                            }

                            else -> Unit
                        }
                    }
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
        val member = GroupManager.state!!.group.members.find { it.id == id }!!
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
        navigator.openGroupQrActivity(GroupManager.state!!.getGroupId())
    }

    private fun Single<Group>.process(): Disposable {
        return this
                .subscribeOn(schedulers.backgroundThread())
                .observeOn(schedulers.mainThread())
                .subscribe({}, { Log.d(TAG, "GroupManager returned error", it) })
    }
}