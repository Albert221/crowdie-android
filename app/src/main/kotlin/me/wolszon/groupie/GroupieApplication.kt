package me.wolszon.groupie

import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import me.wolszon.groupie.di.DaggerAppComponent

class GroupieApplication : DaggerApplication() {
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().create(this)
    }

    companion object {
        const val API_BASE_URL = "http://dupa"
    }
}