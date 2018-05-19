package me.wolszon.crowdie.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import me.wolszon.crowdie.android.services.CoordsTrackerModule
import me.wolszon.crowdie.android.services.CoordsTrackerService
import me.wolszon.crowdie.android.ui.group.GroupActivity
import me.wolszon.crowdie.android.ui.group.GroupModule
import me.wolszon.crowdie.android.ui.group.tabs.map.MapTab
import me.wolszon.crowdie.android.ui.group.tabs.members.MembersTab
import me.wolszon.crowdie.android.ui.group.tabs.qr.QrTab
import me.wolszon.crowdie.android.ui.landing.LandingActivity
import me.wolszon.crowdie.android.ui.landing.LandingModule

@Module
abstract class ActivityBuilder {
    @ContributesAndroidInjector(modules = [LandingModule::class])
    abstract fun bindLandingActivity(): LandingActivity

    @ContributesAndroidInjector(modules = [GroupModule::class])
    abstract fun bindGroupActivity(): GroupActivity

    @ContributesAndroidInjector(modules = [GroupModule::class])
    abstract fun bindGroupMapTab(): MapTab

    @ContributesAndroidInjector(modules = [GroupModule::class])
    abstract fun bindGroupMembersTab(): MembersTab

    @ContributesAndroidInjector(modules = [GroupModule::class])
    abstract fun bindGroupQrTab(): QrTab

    @ContributesAndroidInjector(modules = [CoordsTrackerModule::class])
    abstract fun bindCoordsTrackerService(): CoordsTrackerService
}