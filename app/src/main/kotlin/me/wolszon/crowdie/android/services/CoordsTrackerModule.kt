package me.wolszon.crowdie.android.services

import dagger.Module
import dagger.Provides
import me.wolszon.crowdie.api.domain.GroupManager
import me.wolszon.crowdie.base.Schedulers

@Module
class CoordsTrackerModule {
    @Provides
    fun providePresenter(groupManager: GroupManager, schedulers: Schedulers) =
            CoordsTrackerPresenter(groupManager, schedulers)
}