package me.wolszon.crowdie.android.ui.group.tabs.members

import dagger.Module
import dagger.Provides
import me.wolszon.crowdie.android.ui.group.tabs.members.adapter.MemberClickEventSubject
import me.wolszon.crowdie.android.ui.group.tabs.members.adapter.MembersListAdapter
import me.wolszon.crowdie.api.domain.GroupManager
import me.wolszon.crowdie.base.Schedulers

@Module
class MembersTabModule {
    @Provides
    fun provideMembersListAdapter(memberClickEventSubject: MemberClickEventSubject) =
            MembersListAdapter(memberClickEventSubject)

    @Provides
    fun provideMembersPresenter(groupManager: GroupManager, schedulers: Schedulers) =
            MembersPresenter(groupManager, schedulers)
}