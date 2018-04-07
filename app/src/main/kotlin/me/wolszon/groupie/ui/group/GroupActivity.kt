package me.wolszon.groupie.ui.group

import android.app.AlertDialog
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.activity_group.*
import me.wolszon.groupie.R
import me.wolszon.groupie.api.models.dataclass.Member
import me.wolszon.groupie.api.state.GroupState
import me.wolszon.groupie.base.BaseActivity
import me.wolszon.groupie.prepare
import me.wolszon.groupie.ui.adapter.MembersListAdapter
import me.wolszon.groupie.ui.groupqr.GroupQrActivity
import javax.inject.Inject

class GroupActivity : BaseActivity(), GroupView, OnMapReadyCallback {
    @Inject lateinit var presenter: GroupPresenter

    private lateinit var map: GoogleMap
    private val markers = hashMapOf<String, Marker>()
    @Inject lateinit var membersListAdapter: MembersListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group)
        setSupportActionBar(toolbar)

        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        membersListAdapter.onMemberClickListener = this::focusMemberOnMap
        membersListAdapter.onMemberPromoteListener = presenter::promoteMember
        membersListAdapter.onMemberSuppressListener = presenter::suppressMember
        membersListAdapter.onMemberBlockListener= presenter::blockMember
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.group_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_qr -> {
            startActivity(GroupQrActivity.createIntent(this, GroupState.groupId))
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        presenter.loadMembers()
    }

    override fun showMembers(members: List<Member>) {
        val freshLoad = markers.isEmpty()
        val markersToDelete = markers.keys.toMutableList()

        val boundsBuilder = LatLngBounds.Builder()
        members.forEach {
            markersToDelete.remove(it.id)

            if (!markers.containsKey(it.id)) {
                // Given member doesn't have corresponding marker, so it's new. Create one
                markers[it.id] = map.addMarker(MarkerOptions().position(LatLng(0.0, 0.0)))

                membersListAdapter.addMember(it)
            } else {
                membersListAdapter.updateMember(it)
            }

            markers[it.id]!!.apply{
                position = LatLng(it.lat.toDouble(), it.lng.toDouble())
                title = it.name

                boundsBuilder.include(position)
            }
        }

        markersToDelete.forEach {
            markers[it]?.remove()
            markers.remove(it)

            membersListAdapter.removeMember(it)
        }

        // Move map's camera boundaries to have it containing all markers
        if (freshLoad) {
            map.moveCamera(CameraUpdateFactory
                    .newLatLngBounds(boundsBuilder.build(), 30))
        } else {
            // TODO: Determine whether map is centered or moved by user, if it's moved, then don't move camera here.
            map.animateCamera(CameraUpdateFactory
                    .newLatLngBounds(boundsBuilder.build(), 30))
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

    override fun displayMemberBlockConfirmation(member: Member, callback: (Boolean) -> Unit) {
        AlertDialog.Builder(this)
                .setTitle("Usuwanie członka")
                .setMessage("Czy na pewno chcesz usunąć %s?".format(member.name))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes) {
                    _, _ ->
                    callback(true)
                }
                .setNegativeButton(android.R.string.no) {
                    _, _ ->
                    callback(false)
                }
                .show()
    }
}