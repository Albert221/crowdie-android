package me.wolszon.groupie.di.modules

import dagger.Module
import dagger.Provides
import me.wolszon.groupie.api.repository.GroupApi
import me.wolszon.groupie.api.repository.GroupRepository
import retrofit2.Retrofit

@Module
class RepositoryModule {
    // Remember to provide repository with interface
    @Provides
    fun provideGroupApi(retrofit: Retrofit) : GroupApi = GroupRepository(retrofit)
}