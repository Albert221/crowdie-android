package me.wolszon.groupie.android.ui.group

import dagger.Module
import dagger.Provides
import me.wolszon.groupie.api.repository.GroupApi
import me.wolszon.groupie.base.Schedulers
import me.wolszon.groupie.android.ui.Navigator

@Module
class GroupModule {
    @Provides
    fun provideNavigator(groupActivity: GroupActivity): Navigator = Navigator(groupActivity)

    @Provides
    fun providePresenter(schedulers: Schedulers, groupApi: GroupApi, navigator: Navigator) =
            GroupPresenter(schedulers, groupApi, navigator)
}