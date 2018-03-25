package me.wolszon.groupie.di

import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import me.wolszon.groupie.GroupieApplication
import me.wolszon.groupie.di.modules.NetworkModule
import me.wolszon.groupie.di.modules.RepositoryModule
import javax.inject.Singleton

@Singleton
@Component( modules = [
    AndroidSupportInjectionModule::class,
    AppModule::class,
    NetworkModule::class,
    RepositoryModule::class,
    ActivityBuilder::class] )
internal interface AppComponent : AndroidInjector<GroupieApplication> {
    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<GroupieApplication>()
}