package me.wolszon.crowdie.android.ui.group.tabs.members.adapter

import io.reactivex.ObservableSource
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject

class MemberClickEventSubject : Observer<String>, ObservableSource<String> {
    private val publishSubject = PublishSubject.create<String>()

    override fun onComplete() = publishSubject.onComplete()
    override fun onSubscribe(d: Disposable) = publishSubject.onSubscribe(d)
    override fun onNext(t: String) = publishSubject.onNext(t)
    override fun onError(e: Throwable) = publishSubject.onError(e)
    override fun subscribe(observer: Observer<in String>) = publishSubject.subscribe(observer)
    fun subscribe(observer: (String) -> Unit) = publishSubject.subscribe(observer)
}