package me.wolszon.groupie.base

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

// Should be extended by all Presenters.
open class BasePresenter<T : BaseView> {
    var compositeObservable = CompositeDisposable()
    var view : T? = null
    val isSubscribed : Boolean
        get() = view != null

    open fun subscribe(view: T) {
        compositeObservable.dispose()
        compositeObservable = CompositeDisposable()
        this.view = view
    }

    open fun unsubscribe() {
        view = null
        compositeObservable.dispose()
    }

    fun run(disposable: () -> Disposable) {
        compositeObservable.add(disposable())
    }
}