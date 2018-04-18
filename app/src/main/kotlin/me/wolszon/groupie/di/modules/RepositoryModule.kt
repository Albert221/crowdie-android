package me.wolszon.groupie.di.modules

import dagger.Module
import dagger.Provides
import me.wolszon.groupie.android.Preferences
import me.wolszon.groupie.api.ApiGroupManager
import me.wolszon.groupie.api.GroupAdmin
import me.wolszon.groupie.api.GroupClient
import me.wolszon.groupie.api.GroupManager
import me.wolszon.groupie.api.repository.GroupApi
import me.wolszon.groupie.api.repository.GroupRepository
import me.wolszon.groupie.base.Schedulers
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class RepositoryModule {
    @Provides
    fun provideGroupApi(retrofit: Retrofit): GroupApi = GroupRepository(retrofit)

    @Provides
    fun provideGroupClient(groupManager: GroupManager): GroupClient = groupManager

    @Provides
    fun provideGroupAdmin(groupManager: GroupManager): GroupAdmin = groupManager

    @Provides
    @Singleton
    fun provideGroupManager(preferences: Preferences, groupApi: GroupApi, schedulers: Schedulers): GroupManager =
            ApiGroupManager(preferences, groupApi, schedulers)
}