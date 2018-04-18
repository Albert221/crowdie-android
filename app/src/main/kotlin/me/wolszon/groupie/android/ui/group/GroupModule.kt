package me.wolszon.groupie.android.ui.group

import dagger.Module
import dagger.Provides
import me.wolszon.groupie.android.ui.Navigator
import me.wolszon.groupie.api.GroupManager
import me.wolszon.groupie.base.Schedulers

@Module
class GroupModule {
    @Provides
    fun provideNavigator(groupActivity: GroupActivity): Navigator = Navigator(groupActivity)

    @Provides
    fun providePresenter(groupManager: GroupManager, navigator: Navigator, schedulers: Schedulers) =
            GroupPresenter(groupManager, navigator, schedulers)
}