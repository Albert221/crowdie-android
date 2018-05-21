package me.wolszon.crowdie.android.ui.group

import dagger.Module
import dagger.Provides
import me.wolszon.crowdie.android.ui.Navigator
import me.wolszon.crowdie.android.ui.group.tabs.map.MapPresenter
import me.wolszon.crowdie.android.ui.group.tabs.members.MembersPresenter
import me.wolszon.crowdie.android.ui.group.tabs.members.adapter.MemberClickEventSubject
import me.wolszon.crowdie.android.ui.group.tabs.qr.QrPresenter
import me.wolszon.crowdie.api.domain.GroupManager
import me.wolszon.crowdie.base.Schedulers
import javax.inject.Singleton

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
    fun provideMemberClickEventSubject(): MemberClickEventSubject = MemberClickEventSubject.create()

    @Provides
    fun provideMembersPresenter(groupManager: GroupManager, schedulers: Schedulers) =
            MembersPresenter(groupManager, schedulers)

    @Provides
    fun provideQrPresenter() = QrPresenter()
}