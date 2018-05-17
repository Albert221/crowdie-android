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
            // Request an update every 3 seconds
            Observable.interval(3, TimeUnit.SECONDS)
                    .subscribe { run {
                        groupManager
                                .update()
                                .process()
                                .subscribe()
                    } }
        }

        run {
            groupManager
                    .getGroupObservable()
                    .subscribeOn(schedulers.backgroundThread())
                    .observeOn(schedulers.mainThread())
                    .subscribe {
                        when (it.event) {
                            StateFeed.Event.KICK -> {
                                Log.i(TAG, "User has probably been kicked.")

                                view.informAboutBeingKicked()
                                navigator.openLandingActivity()
                            }

                            else -> Unit
                        }
                    }
        }
    }

    fun leaveGroup() {
        run {
            groupManager
                    .leaveGroup()
                    .process()
                    .subscribe { _ -> navigator.openLandingActivity() }
        }
    }

    private fun Single<Group>.process(): Single<Group> {
        return this
                .subscribeOn(schedulers.backgroundThread())
                .observeOn(schedulers.mainThread())
    }
}