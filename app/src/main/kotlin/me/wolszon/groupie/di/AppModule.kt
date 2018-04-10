package me.wolszon.groupie.di

import android.content.Context
import dagger.Binds
import dagger.Module
import me.wolszon.groupie.android.GroupieApplication

@Module
abstract class AppModule {
    // Binds acts like provide, but it just binds the class instance with its interface
    @Binds
    abstract fun provideContext(application: GroupieApplication) : Context
}