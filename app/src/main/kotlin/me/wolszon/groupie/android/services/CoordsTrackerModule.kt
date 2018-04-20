package me.wolszon.groupie.android.services

import dagger.Module
import dagger.Provides
import me.wolszon.groupie.api.domain.GroupManager
import me.wolszon.groupie.base.Schedulers

@Module
class CoordsTrackerModule {
    @Provides
    fun providePresenter(groupManager: GroupManager, schedulers: Schedulers) =
            CoordsTrackerPresenter(groupManager, schedulers)
}