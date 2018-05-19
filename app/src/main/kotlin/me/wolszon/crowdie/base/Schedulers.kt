package me.wolszon.crowdie.base

import io.reactivex.Scheduler

interface Schedulers {
    fun mainThread() : Scheduler
    fun backgroundThread() : Scheduler
}