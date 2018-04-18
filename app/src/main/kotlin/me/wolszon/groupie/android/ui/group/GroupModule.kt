package me.wolszon.groupie.android.ui.group

import dagger.Module
import dagger.Provides
import me.wolszon.groupie.android.ui.Navigator
import me.wolszon.groupie.api.GroupManager

@Module
class GroupModule {
    @Provides
    fun provideNavigator(groupActivity: GroupActivity): Navigator = Navigator(groupActivity)

    @Provides
    fun providePresenter(groupManager: GroupManager, navigator: Navigator) =
            GroupPresenter(groupManager, navigator)
}