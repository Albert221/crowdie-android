package me.wolszon.groupie.android.ui.landing

import io.reactivex.Single
import io.reactivex.disposables.Disposable
import me.wolszon.groupie.base.BasePresenter
import me.wolszon.groupie.android.ui.Navigator
import me.wolszon.groupie.api.domain.GroupManager
import me.wolszon.groupie.base.Preferences
import me.wolszon.groupie.api.models.dataclass.Group
import me.wolszon.groupie.base.Schedulers

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