package me.wolszon.groupie.android.ui.group

import dagger.Module
import dagger.Provides
import me.wolszon.groupie.android.ui.Navigator
import me.wolszon.groupie.android.ui.group.tabs.map.MapPresenter
import me.wolszon.groupie.android.ui.group.tabs.members.MembersPresenter
import me.wolszon.groupie.android.ui.group.tabs.qr.QrPresenter
import me.wolszon.groupie.api.domain.GroupManager
import me.wolszon.groupie.base.Schedulers

@Module
class GroupModule {
    @Provides
    fun provideNavigator(groupActivity: GroupActivity): Navigator = Navigator(groupActivity)

    @Provides
    fun providePresenter(groupManager: GroupManager, navigator: Navigator, schedulers: Schedulers) =
            GroupPresenter(groupManager, navigator, schedulers)

    @Provides
    fun provideMapPresenter(groupManager: GroupManager, schedulers: Schedulers) =
            MapPresenter(groupManager, schedulers)

    @Provides
    fun provideMembersPresenter(groupManager: GroupManager, schedulers: Schedulers) =
            MembersPresenter(groupManager, schedulers)

    @Provides
    fun provideQrPresenter() = QrPresenter()
}