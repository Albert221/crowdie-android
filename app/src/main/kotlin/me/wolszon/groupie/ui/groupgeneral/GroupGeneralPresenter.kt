package me.wolszon.groupie.ui.groupgeneral

import me.wolszon.groupie.api.repository.GroupApi
import me.wolszon.groupie.base.BasePresenter
import me.wolszon.groupie.base.Schedulers

class GroupGeneralPresenter(val schedulers : Schedulers, val groupApi : GroupApi)
    : BasePresenter<GroupGeneralView>() {
    val id = ""

    fun loadMembers() {
        compositeObservable.add(
                groupApi.find(id)
                        .subscribeOn(schedulers.backgroundThread())
                        .observeOn(schedulers.mainThread())
                        .subscribe({
                            view?.showMembersMarkers(it.members)
                        }, { view?.showErrorDialog(it) })
        )
    }
}