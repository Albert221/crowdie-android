package me.wolszon.crowdie.di.modules

import dagger.Module
import dagger.Provides
import me.wolszon.crowdie.base.Preferences
import me.wolszon.crowdie.api.domain.ApiGroupManager
import me.wolszon.crowdie.api.domain.GroupManager
import me.wolszon.crowdie.api.retrofit.V1RetrofitApi
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class RepositoryModule {
    @Provides
    fun provideV1RetrofitApi(retrofit: Retrofit): V1RetrofitApi = retrofit.create(V1RetrofitApi::class.java)

    @Provides
    @Singleton
    fun provideGroupManager(preferences: Preferences, v1RetrofitApi: V1RetrofitApi): GroupManager =
            ApiGroupManager(preferences, v1RetrofitApi)
}