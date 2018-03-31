package me.wolszon.groupie.ui.group

import me.wolszon.groupie.api.repository.GroupApi
import me.wolszon.groupie.base.BasePresenter
import me.wolszon.groupie.base.Schedulers

class GroupPresenter(val schedulers : Schedulers, val groupApi : GroupApi) : BasePresenter<GroupView>() {
    val id = "d175a80a-399a-4c89-b05a-1b8e2decab57"

    fun loadMembers() {
        compositeObservable.add(
                groupApi.find(id)
                        .subscribeOn(schedulers.backgroundThread())
                        .observeOn(schedulers.mainThread())
                        .subscribe({
                            view?.showMembers(it.members)
                        }, { view?.showErrorDialog(it) })
        )
    }
}