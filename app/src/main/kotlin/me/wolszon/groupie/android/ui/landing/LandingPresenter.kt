package me.wolszon.groupie.android.ui.landing

import io.reactivex.Single
import io.reactivex.disposables.Disposable
import me.wolszon.groupie.base.BasePresenter
import me.wolszon.groupie.android.ui.Navigator
import me.wolszon.groupie.api.GroupManager
import me.wolszon.groupie.api.models.dataclass.Group
import me.wolszon.groupie.base.Schedulers

class LandingPresenter(private val groupManager: GroupManager,
                       private val navigator: Navigator,
                       private val schedulers: Schedulers) : BasePresenter<LandingView>() {
    fun createGroup() {
        run {
            groupManager.newGroup().process()
        }
    }

    fun joinExistingGroup(groupId: String) {
        run {
            groupManager.joinGroup(groupId).process()
        }
    }

    private fun Single<Group>.process(): Disposable {
        return this
                .subscribeOn(schedulers.backgroundThread())
                .observeOn(schedulers.mainThread())
                .subscribe({ navigator.openGroupActivity() }, { view?.showErrorDialog(it) })
    }
}