package me.wolszon.crowdie.android.ui.group.tabs.map

import dagger.Module
import dagger.Provides
import me.wolszon.crowdie.api.domain.GroupManager
import me.wolszon.crowdie.base.Schedulers

@Module
class MapTabModule {
    @Provides
    fun provideMapPresenter(groupManager: GroupManager, schedulers: Schedulers) =
            MapPresenter(groupManager, schedulers)
}