package me.wolszon.groupie.di.modules

import dagger.Module
import dagger.Provides
import me.wolszon.groupie.api.domain.Preferences
import me.wolszon.groupie.api.domain.ApiGroupManager
import me.wolszon.groupie.api.domain.GroupAdmin
import me.wolszon.groupie.api.domain.GroupClient
import me.wolszon.groupie.api.domain.GroupManager
import me.wolszon.groupie.api.repository.GroupApi
import me.wolszon.groupie.api.repository.GroupRepository
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
    fun provideGroupManager(preferences: Preferences, groupApi: GroupApi): GroupManager =
            ApiGroupManager(preferences, groupApi)
}