package me.wolszon.groupie

import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import me.wolszon.groupie.api.state.GroupState
import me.wolszon.groupie.di.DaggerAppComponent

class GroupieApplication : DaggerApplication() {
    init {
        // TODO: It will be populated with newly created or joined room's id
        GroupState.groupId = "d175a80a-399a-4c89-b05a-1b8e2decab57"
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().create(this)
    }

    companion object {
        const val API_BASE_URL = "http://192.168.1.30:8080"
    }
}