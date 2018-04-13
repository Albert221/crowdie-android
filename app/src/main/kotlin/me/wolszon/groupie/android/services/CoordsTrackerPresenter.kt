package me.wolszon.groupie.android.services

import com.google.android.gms.location.LocationResult
import me.wolszon.groupie.api.repository.GroupApi
import me.wolszon.groupie.base.BasePresenter
import me.wolszon.groupie.base.Schedulers

class CoordsTrackerPresenter(private val schedulers: Schedulers,
                             private val groupApi: GroupApi) : BasePresenter<CoordsTrackerView>() {
    lateinit var memberId: String

    fun sendCoords(location: LocationResult) {
        run {
            groupApi.sendMemberCoordsBit(
                    memberId,
                    location.lastLocation.latitude.toFloat(),
                    location.lastLocation.longitude.toFloat()
            )
                    .subscribeOn(schedulers.backgroundThread())
                    .observeOn(schedulers.mainThread())
                    .subscribe({
                        //
                    }, { view?.showErrorDialog(it) })
        }
    }
}