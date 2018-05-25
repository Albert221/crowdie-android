package me.wolszon.crowdie.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import me.wolszon.crowdie.android.services.CoordsTrackerModule
import me.wolszon.crowdie.android.services.CoordsTrackerService
import me.wolszon.crowdie.android.ui.group.GroupActivity
import me.wolszon.crowdie.android.ui.group.GroupModule
import me.wolszon.crowdie.android.ui.group.GroupTabsProvider
import me.wolszon.crowdie.android.ui.landing.LandingActivity
import me.wolszon.crowdie.android.ui.landing.LandingModule

@Module
abstract class ActivityBuilder {
    @ContributesAndroidInjector(modules = [LandingModule::class])
    abstract fun bindLandingActivity(): LandingActivity

    @PerActivity
    @ContributesAndroidInjector(modules = [GroupModule::class, GroupTabsProvider::class])
    abstract fun bindGroupActivity(): GroupActivity

    @ContributesAndroidInjector(modules = [CoordsTrackerModule::class])
    abstract fun bindCoordsTrackerService(): CoordsTrackerService
}