package me.wolszon.crowdie.android.ui.group

import dagger.Module
import dagger.android.ContributesAndroidInjector
import me.wolszon.crowdie.android.ui.group.tabs.map.MapTab
import me.wolszon.crowdie.android.ui.group.tabs.map.MapTabModule
import me.wolszon.crowdie.android.ui.group.tabs.members.MembersTab
import me.wolszon.crowdie.android.ui.group.tabs.members.MembersTabModule
import me.wolszon.crowdie.android.ui.group.tabs.qr.QrTab
import me.wolszon.crowdie.android.ui.group.tabs.qr.QrTabModule

@Module
abstract class GroupTabsProvider {
    @ContributesAndroidInjector(modules = [MapTabModule::class])
    abstract fun provideMapTab(): MapTab

    @ContributesAndroidInjector(modules = [MembersTabModule::class])
    abstract fun provideMembersTab(): MembersTab

    @ContributesAndroidInjector(modules = [QrTabModule::class])
    abstract fun provideQrTab(): QrTab
}