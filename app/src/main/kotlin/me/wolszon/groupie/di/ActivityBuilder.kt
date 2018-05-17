package me.wolszon.groupie.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import me.wolszon.groupie.android.services.CoordsTrackerModule
import me.wolszon.groupie.android.services.CoordsTrackerService
import me.wolszon.groupie.android.ui.group.GroupActivity
import me.wolszon.groupie.android.ui.group.GroupModule
import me.wolszon.groupie.android.ui.group.tabs.map.MapTab
import me.wolszon.groupie.android.ui.group.tabs.members.MembersTab
import me.wolszon.groupie.android.ui.group.tabs.qr.QrTab
import me.wolszon.groupie.android.ui.landing.LandingActivity
import me.wolszon.groupie.android.ui.landing.LandingModule

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