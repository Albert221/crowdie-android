package me.wolszon.crowdie.android.ui.group

import dagger.Module
import dagger.Provides
import me.wolszon.crowdie.android.ui.Navigator
import me.wolszon.crowdie.android.ui.group.tabs.members.adapter.MemberClickEventSubject
import me.wolszon.crowdie.api.domain.GroupManager
import me.wolszon.crowdie.base.Schedulers
import me.wolszon.crowdie.di.PerActivity

@Module
class GroupModule {
    @Provides
    fun provideNavigator(groupActivity: GroupActivity): Navigator = Navigator(groupActivity)

    @Provides
    fun providePresenter(groupManager: GroupManager, navigator: Navigator, schedulers: Schedulers) =
            GroupPresenter(groupManager, navigator, schedulers)

    @Provides
    @PerActivity
    fun provideMemberClickEventSubject(): MemberClickEventSubject = MemberClickEventSubject()
}