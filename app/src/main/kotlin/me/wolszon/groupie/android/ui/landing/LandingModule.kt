package me.wolszon.groupie.android.ui.landing

import dagger.Module
import dagger.Provides
import me.wolszon.groupie.api.repository.GroupApi
import me.wolszon.groupie.base.Schedulers
import me.wolszon.groupie.android.ui.Navigator

@Module
class LandingModule {
    @Provides
    fun provideNavigator(landingActivity: LandingActivity): Navigator = Navigator(landingActivity)

    @Provides
    fun providePresenter(schedulers: Schedulers, groupApi: GroupApi, navigator: Navigator) =
            LandingPresenter(schedulers, groupApi, navigator)
}