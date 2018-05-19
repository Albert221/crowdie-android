package me.wolszon.crowdie.android.services

import com.google.android.gms.location.LocationResult
import me.wolszon.crowdie.api.domain.GroupManager
import me.wolszon.crowdie.api.domain.StateFeed
import me.wolszon.crowdie.base.BasePresenter
import me.wolszon.crowdie.base.Schedulers
import me.wolszon.crowdie.utils.CurrentPositionUtil

class CoordsTrackerPresenter(private val groupManager: GroupManager,
                             private val schedulers: Schedulers) : BasePresenter<CoordsTrackerView>() {
    override fun subscribe(view: CoordsTrackerView) {
        super.subscribe(view)

        run {
            groupManager
                    .getGroupObservable()
                    .subscribe {
                        when(it.event) {
                            StateFeed.Event.KICK,
                            StateFeed.Event.LEAVE -> {
                                view.stopService()
                            }

                            else -> Unit
                        }
                    }
        }
    }

    fun sendCoords(location: LocationResult) {
        val latitude = location.lastLocation.latitude.toFloat()
        val longitude = location.lastLocation.longitude.toFloat()

        CurrentPositionUtil.latitude = latitude
        CurrentPositionUtil.longitude = longitude

        run {
            groupManager.sendCoords(latitude, longitude)
                    .subscribeOn(schedulers.backgroundThread())
                    .observeOn(schedulers.mainThread())
                    .subscribe()
        }
    }
}