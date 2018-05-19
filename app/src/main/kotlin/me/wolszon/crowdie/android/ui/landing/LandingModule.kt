package me.wolszon.crowdie.android.ui.landing

import dagger.Module
import dagger.Provides
import me.wolszon.crowdie.android.ui.Navigator
import me.wolszon.crowdie.api.domain.GroupManager
import me.wolszon.crowdie.base.Preferences
import me.wolszon.crowdie.base.Schedulers

@Module
class LandingModule {
    @Provides
    fun provideNavigator(landingActivity: LandingActivity): Navigator = Navigator(landingActivity)

    @Provides
    fun providePresenter(groupManager: GroupManager, navigator: Navigator, schedulers: Schedulers,
                         preferences: Preferences) =
            LandingPresenter(groupManager, navigator, schedulers, preferences)
}