package me.wolszon.crowdie.base

import android.app.Service
import dagger.android.AndroidInjection

abstract class BaseService : Service() {
    override fun onCreate() {
        super.onCreate()

        AndroidInjection.inject(this)
    }
}