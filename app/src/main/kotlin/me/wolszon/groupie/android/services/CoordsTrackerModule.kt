package me.wolszon.groupie.android.services

import dagger.Module
import dagger.Provides
import me.wolszon.groupie.api.repository.GroupApi
import me.wolszon.groupie.base.Schedulers

@Module
class CoordsTrackerModule {
    @Provides
    fun providePresenter(schedulers: Schedulers, groupApi: GroupApi) =
            CoordsTrackerPresenter(schedulers, groupApi)
}