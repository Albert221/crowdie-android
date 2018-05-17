package me.wolszon.groupie.di.modules

import android.content.Context
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import me.wolszon.groupie.BuildConfig
import me.wolszon.groupie.android.AndroidPreferences
import me.wolszon.groupie.api.okhttp.UserTokenInterceptor
import me.wolszon.groupie.base.Preferences
import me.wolszon.groupie.base.ApplicationSchedulers
import me.wolszon.groupie.base.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

// Provides network-related classes
@Module
class NetworkModule {
    @Provides
    fun provideApplicationSchedulers(): Schedulers = ApplicationSchedulers()

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder().build()

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient, moshi: Moshi): Retrofit {
        val apiBaseUrl = when (BuildConfig.BUILD_TYPE) {
            "release" -> ""
            "staging" -> "http://139.59.147.215:8080"
            /* "debug", */ else -> "http://192.168.1.30:8080"
        }

        return Retrofit.Builder()
                .client(client)
                .baseUrl(apiBaseUrl)
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(preferences: Preferences): OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor(UserTokenInterceptor(preferences))
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build()
    }

    @Provides
    fun providePreferences(context: Context): Preferences = AndroidPreferences(context)
}