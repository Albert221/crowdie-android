package me.wolszon.groupie.android.ui.group.tabs

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.group_tab_map.*
import me.wolszon.groupie.R
import me.wolszon.groupie.api.models.dataclass.Member
import me.wolszon.groupie.base.BaseView
import me.wolszon.groupie.utils.isVisible
import javax.inject.Inject

class MapTab : DaggerFragment(), OnMapReadyCallback, BaseView {
    @Inject lateinit var presenter: MapPresenter

    private lateinit var map: GoogleMap
    private val markers = hashMapOf<String, Marker>()

    private lateinit var lastLatLngBounds: LatLngBounds
    private var mapReady = false
    private var mapCentringInterrupted = false
    private var mapDuringMoving = false
    private var mapAlreadyLoaded = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.group_tab_map, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        centerButton.setOnClickListener { centerMap() }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        map.mapType = GoogleMap.MAP_TYPE_HYBRID
        map.setMaxZoomPreference(19f)
        map.uiSettings.isMapToolbarEnabled = false
        map.setOnCameraMoveStartedListener {
            if (it == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
                interruptMapCentering()
            }
        }

        mapReady = true

        presenter.subscribe(this)
    }

    override fun onResume() {
        super.onResume()

        presenter.subscribe(this)
    }

    override fun onPause() {
        super.onPause()

        presenter.unsubscribe()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        presenter.unsubscribe()
    }

    fun showMembers(members: List<Member>) {
        val markersToDelete = markers.keys.toMutableList()
        val boundsBuilder = LatLngBounds.Builder()
        members.forEach {
            markersToDelete.remove(it.id)

            if (!markers.containsKey(it.id)) {
                // Given member doesn't have corresponding marker, so it's new. Create one
                markers[it.id] = map.addMarker(MarkerOptions().position(LatLng(0.0, 0.0)))
            }

            markers[it.id]!!.apply{
                position = it.getLatLng()
                title = it.name

                boundsBuilder.include(position)
            }
        }

        markersToDelete.forEach {
            markers[it]?.remove()
            markers.remove(it)
        }

        if (markers.size > 0) {
            // Move map's camera boundaries to have it containing all markers
            lastLatLngBounds = boundsBuilder.build()

            if (!mapAlreadyLoaded) {
                map.moveCamera(
                        paddedLngBoundsCameraUpdate(lastLatLngBounds)
                )
            } else if (!mapCentringInterrupted) {
                mapDuringMoving = true
                map.animateCamera(
                        paddedLngBoundsCameraUpdate(lastLatLngBounds),
                        MapMoveCallback()
                )
            }
        }

        mapAlreadyLoaded = true
    }

    private fun centerMap() {
        centerButton.isVisible = false

        mapCentringInterrupted = false
        mapDuringMoving = true
        map.animateCamera(
                paddedLngBoundsCameraUpdate(lastLatLngBounds),
                MapMoveCallback()
        )
    }

    private fun interruptMapCentering() {
        centerButton?.isVisible = true

        mapCentringInterrupted = true
        mapDuringMoving = false
    }

    private fun paddedLngBoundsCameraUpdate(bounds: LatLngBounds): CameraUpdate {
        val width = resources.displayMetrics.widthPixels
        val height = TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300f, resources.displayMetrics)
                .toInt()
        val padding = (width * 0.12).toInt()

        return CameraUpdateFactory
                .newLatLngBounds(bounds, width, height, padding)
    }

    override fun showErrorDialog(e: Throwable) {
        TODO("not implemented")
    }

    private inner class MapMoveCallback : GoogleMap.CancelableCallback {
        override fun onFinish() {
            mapDuringMoving = false
        }

        override fun onCancel() {
            if(mapDuringMoving) {
                return
            }

            interruptMapCentering()
        }
    }
}