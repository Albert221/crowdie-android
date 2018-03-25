package me.wolszon.groupie.ui.groupgeneral

import android.os.Bundle
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import me.wolszon.groupie.R
import me.wolszon.groupie.api.models.dataclass.Member
import me.wolszon.groupie.base.BaseActivity
import javax.inject.Inject

class GroupGeneralActivity : BaseActivity(), GroupGeneralView, OnMapReadyCallback {
    @Inject lateinit var presenter: GroupGeneralPresenter

    private lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_general)

        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        presenter.subscribe(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unsubscribe()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        presenter.loadMembers()
    }

    override fun showMembersMarkers(members: List<Member>) {
        map.clear()

        members.forEach {
            val marker = MarkerOptions()
                    .title(it.name)
                    .position(LatLng(it.lat, it.lng))

            map.addMarker(marker)
        }
    }
}