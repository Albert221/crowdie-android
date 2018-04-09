package me.wolszon.groupie.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import me.wolszon.groupie.ui.group.GroupActivity
import me.wolszon.groupie.ui.group.GroupModule
import me.wolszon.groupie.ui.group.GroupQrActivity
import me.wolszon.groupie.ui.landing.LandingActivity
import me.wolszon.groupie.ui.landing.LandingModule

@Module
abstract class ActivityBuilder {
    @ContributesAndroidInjector(modules = [LandingModule::class])
    abstract fun bindLandingActivity(): LandingActivity

    @ContributesAndroidInjector(modules = [GroupModule::class])
    abstract fun bindGroupActivity(): GroupActivity

    @ContributesAndroidInjector
    abstract fun bindGroupQrActivity(): GroupQrActivity
}