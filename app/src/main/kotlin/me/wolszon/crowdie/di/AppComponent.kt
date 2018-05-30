package me.wolszon.crowdie.di

import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import me.wolszon.crowdie.android.Application
import me.wolszon.crowdie.di.modules.NetworkModule
import me.wolszon.crowdie.di.modules.RepositoryModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class,
    AppModule::class,
    NetworkModule::class,
    RepositoryModule::class,
    ActivityBuilder::class
])
internal interface AppComponent : AndroidInjector<Application> {
    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<Application>()
}