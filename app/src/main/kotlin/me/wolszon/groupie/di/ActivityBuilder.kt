package me.wolszon.groupie.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import me.wolszon.groupie.ui.groupgeneral.GroupGeneralActivity
import me.wolszon.groupie.ui.groupgeneral.GroupGeneralModule

@Module
abstract class ActivityBuilder {
    @ContributesAndroidInjector(modules = [GroupGeneralModule::class])
    abstract fun bindGroupGeneralActivity() : GroupGeneralActivity
}