package me.wolszon.crowdie.android.ui.landing

import io.reactivex.Single
import io.reactivex.disposables.Disposable
import me.wolszon.crowdie.base.BasePresenter
import me.wolszon.crowdie.android.ui.Navigator
import me.wolszon.crowdie.api.domain.GroupManager
import me.wolszon.crowdie.base.Preferences
import me.wolszon.crowdie.api.models.dataclass.Group
import me.wolszon.crowdie.base.Schedulers

class LandingPresenter(private val groupManager: GroupManager,
                       private val navigator: Navigator,
                       private val schedulers: Schedulers,
                       private val preferences: Preferences) : BasePresenter<LandingView>() {
    override fun subscribe(view: LandingView) {
        super.subscribe(view)

        if (!checkUsernameSet()) {
            view.promptForUsername()
        }
    }

    private fun checkUsernameSet(): Boolean = preferences.username.isNotEmpty()

    fun createGroup() = run { groupManager.newGroup().process() }

    fun joinExistingGroup(groupId: String) = run { groupManager.joinGroup(groupId).process() }

    fun setUsername(username: String) {
        preferences.username = username
    }

    fun tryJoiningLastGroup() {
        val lastJoinedGroup = preferences.lastJoinedGroup ?: return

        run {
            groupManager.joinGroup(lastJoinedGroup)
                    .subscribeOn(schedulers.backgroundThread())
                    .observeOn(schedulers.mainThread())
                    .subscribe({ navigator.openGroupActivity() }, { })
        }
    }

    private fun Single<Group>.process(): Disposable {
        return this
                .subscribeOn(schedulers.backgroundThread())
                .observeOn(schedulers.mainThread())
                .subscribe({ navigator.openGroupActivity() }, { view?.showErrorDialog(it) })
    }
}