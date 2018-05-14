package me.wolszon.groupie.android.ui.group.tabs

import io.reactivex.Observable
import me.wolszon.groupie.api.domain.GroupManager
import me.wolszon.groupie.base.BasePresenter

class QrPresenter : BasePresenter<QrTab>() {
    fun getGroupId(): Observable<String> {
        return Observable.just(GroupManager.state!!.getGroupId())
    }
}