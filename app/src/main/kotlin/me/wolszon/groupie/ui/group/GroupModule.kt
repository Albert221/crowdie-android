package me.wolszon.groupie.ui.group

import dagger.Module
import dagger.Provides
import me.wolszon.groupie.api.repository.GroupApi
import me.wolszon.groupie.base.Schedulers

@Module
class GroupModule {
    @Provides
    fun providePresenter(schedulers: Schedulers, groupApi: GroupApi)
            = GroupPresenter(schedulers, groupApi)
}