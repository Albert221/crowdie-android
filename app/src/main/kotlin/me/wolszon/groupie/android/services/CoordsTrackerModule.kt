package me.wolszon.groupie.android.services

import dagger.Module
import dagger.Provides
import me.wolszon.groupie.api.GroupManager

@Module
class CoordsTrackerModule {
    @Provides
    fun providePresenter(groupManager: GroupManager) = CoordsTrackerPresenter(groupManager)
}