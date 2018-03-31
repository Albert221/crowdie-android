package me.wolszon.groupie.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import me.wolszon.groupie.ui.group.GroupActivity
import me.wolszon.groupie.ui.group.GroupModule

@Module
abstract class ActivityBuilder {
    @ContributesAndroidInjector(modules = [GroupModule::class])
    abstract fun bindGroupGeneralActivity() : GroupActivity
}