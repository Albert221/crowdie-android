package me.wolszon.crowdie.di.modules

import dagger.Module
import dagger.Provides
import me.wolszon.crowdie.base.Preferences
import me.wolszon.crowdie.api.domain.ApiGroupManager
import me.wolszon.crowdie.api.domain.GroupManager
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class RepositoryModule {
    @Provides
    @Singleton
    fun provideGroupManager(preferences: Preferences, retrofit: Retrofit): GroupManager =
            ApiGroupManager(preferences, retrofit)
}