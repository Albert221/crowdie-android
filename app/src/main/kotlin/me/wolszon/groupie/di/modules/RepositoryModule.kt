package me.wolszon.groupie.di.modules

import dagger.Module
import dagger.Provides
import me.wolszon.groupie.base.Preferences
import me.wolszon.groupie.api.domain.ApiGroupManager
import me.wolszon.groupie.api.domain.GroupManager
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class RepositoryModule {
    @Provides
    @Singleton
    fun provideGroupManager(preferences: Preferences, retrofit: Retrofit): GroupManager =
            ApiGroupManager(preferences, retrofit)
}