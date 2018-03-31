package me.wolszon.groupie.ui.group

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_group.*
import me.wolszon.groupie.R
import me.wolszon.groupie.api.models.dataclass.Member
import me.wolszon.groupie.base.BaseActivity
import me.wolszon.groupie.prepare
import me.wolszon.groupie.ui.adapter.MembersListAdapter
import javax.inject.Inject

class GroupActivity : BaseActivity(), GroupView, OnMapReadyCallback {
    @Inject lateinit var presenter: GroupPresenter

    private lateinit var map: GoogleMap
    private var markers = hashMapOf<String, Marker>()
    @Inject lateinit var membersListAdapter: MembersListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group)

        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        membersListAdapter.onMemberClickListener = this::focusMemberOnMap
        membersListAdapter.onMemberPromoteListener = presenter::promoteMember
        membersListAdapter.onMemberSuppressListener = presenter::suppressMember
        membersList.apply {
            prepare()
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

            adapter = membersListAdapter
        }

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

    override fun showMembers(members: List<Member>, imperceptibly: Boolean) {
        membersListAdapter.showMembers(members)

        map.apply {
            clear()
            markers.clear()

            val boundsBuilder = LatLngBounds.Builder()
            members.forEach {
                val markerOptions = MarkerOptions()
                        .title(it.name)
                        .position(LatLng(it.lat.toDouble(), it.lng.toDouble()))

                addMarker(markerOptions).apply {
                    boundsBuilder.include(position)
                    markers[it.id] = this
                }
            }

            if (!imperceptibly) {
                moveCamera(CameraUpdateFactory
                        .newLatLngBounds(boundsBuilder.build(), 30))
            }
        }
    }

    override fun focusMemberOnMap(id: String) {
        markers[id]?.apply {
            map.animateCamera(CameraUpdateFactory.newLatLng(this.position), object : GoogleMap.CancelableCallback {
                override fun onFinish() {
                    showInfoWindow()
                }

                override fun onCancel() = Unit
            })
        }
    }

    override fun updateMember(member: Member) {
        member.apply {
            markers[id]?.position = LatLng(lat.toDouble(), lng.toDouble())
            membersListAdapter.updateMember(this)
        }
    }
}