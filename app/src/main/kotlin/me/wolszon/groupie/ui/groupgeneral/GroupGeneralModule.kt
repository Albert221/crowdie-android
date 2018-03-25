package me.wolszon.groupie.ui.groupgeneral

import dagger.Module
import dagger.Provides
import me.wolszon.groupie.api.repository.GroupApi
import me.wolszon.groupie.base.Schedulers

@Module
class GroupGeneralModule {
    @Provides
    fun providePresenter(schedulers: Schedulers, groupApi: GroupApi)
            = GroupGeneralPresenter(schedulers, groupApi)
}