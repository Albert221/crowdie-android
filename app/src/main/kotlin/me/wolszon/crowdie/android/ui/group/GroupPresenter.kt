package me.wolszon.crowdie.android.ui.group

import android.util.Log
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import me.wolszon.crowdie.android.ui.Navigator
import me.wolszon.crowdie.api.domain.GroupManager
import me.wolszon.crowdie.api.domain.StateFeed
import me.wolszon.crowdie.api.models.dataclass.Group
import me.wolszon.crowdie.api.models.dataclass.Member
import me.wolszon.crowdie.base.BasePresenter
import me.wolszon.crowdie.base.Schedulers
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