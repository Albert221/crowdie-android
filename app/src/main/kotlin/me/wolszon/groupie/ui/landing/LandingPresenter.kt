package me.wolszon.groupie.ui.landing

import me.wolszon.groupie.api.repository.GroupApi
import me.wolszon.groupie.base.BasePresenter
import me.wolszon.groupie.base.Schedulers
import me.wolszon.groupie.ui.Navigator

class LandingPresenter(private val schedulers: Schedulers,
                       private val groupApi: GroupApi,
                       private val navigator: Navigator) : BasePresenter<LandingView>() {
    fun createGroup() {
        run {
            // FIXME: Hardcoded data
            groupApi.newGroup("John Doe", 54.446838f, 18.571800f)
                    .subscribeOn(schedulers.backgroundThread())
                    .observeOn(schedulers.mainThread())
                    .subscribe({
                        navigator.openGroupActivity(it.id)
                    }, { view?.showErrorDialog(it) })
        }
    }

    fun joinExistingGroup(groupId: String) {
        run {
            groupApi.addMember(groupId, "John Doe", 54.446838f, 18.571800f)
                    .subscribeOn(schedulers.backgroundThread())
                    .observeOn(schedulers.mainThread())
                    .subscribe({
                        navigator.openGroupActivity(groupId)
                    }, { view?.showErrorDialog(it) })
        }
    }
}