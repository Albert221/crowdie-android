package me.wolszon.groupie.base

import io.reactivex.Scheduler

interface Schedulers {
    fun mainThread() : Scheduler
    fun backgroundThread() : Scheduler
}