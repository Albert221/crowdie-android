package me.wolszon.groupie.android.ui.landing

import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import me.wolszon.groupie.base.BasePresenter
import me.wolszon.groupie.android.ui.Navigator
import me.wolszon.groupie.api.GroupManager
import me.wolszon.groupie.api.models.dataclass.Group

class LandingPresenter(private val groupManager: GroupManager,
                       private val navigator: Navigator) : BasePresenter<LandingView>() {

    override fun subscribe(view: LandingView) {
        super.subscribe(view)

        groupManager.subscribe(object : Observer<Group> {
            override fun onComplete() = Unit
            override fun onSubscribe(d: Disposable) = Unit
            override fun onNext(t: Group) = navigator.openGroupActivity()
            override fun onError(e: Throwable) = view.showErrorDialog(e)
        })
    }

    fun createGroup() {
        groupManager.newGroup()
    }

    fun joinExistingGroup(groupId: String) {
        groupManager.joinGroup(groupId)
    }
}