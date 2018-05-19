package me.wolszon.crowdie.base

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

// Should be extended by all Presenters.
open class BasePresenter<T : BaseView> {
    private var compositeObservable = CompositeDisposable()
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

    protected fun run(disposable: () -> Disposable) {
        compositeObservable.add(disposable())
    }
}