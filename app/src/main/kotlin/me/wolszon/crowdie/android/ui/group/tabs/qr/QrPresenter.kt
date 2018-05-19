package me.wolszon.crowdie.android.ui.group.tabs.qr

import io.reactivex.Observable
import me.wolszon.crowdie.api.domain.GroupManager
import me.wolszon.crowdie.base.BasePresenter

class QrPresenter : BasePresenter<QrView>() {
    fun getGroupId(): Observable<String> {
        return Observable.just(GroupManager.state!!.getGroupId())
    }
}