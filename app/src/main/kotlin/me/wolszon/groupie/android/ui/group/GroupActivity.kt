package me.wolszon.groupie.android.ui.group

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.widget.DividerItemDecoration
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.activity_group.*
import me.wolszon.groupie.R
import me.wolszon.groupie.android.services.CoordsTrackerService
import me.wolszon.groupie.api.models.dataclass.Member
import me.wolszon.groupie.base.BaseActivity
import me.wolszon.groupie.utils.prepare
import me.wolszon.groupie.android.ui.adapter.MembersListAdapter
import me.wolszon.groupie.utils.isVisible
import javax.inject.Inject

class GroupActivity : BaseActivity(), GroupView, OnMapReadyCallback {
    @Inject lateinit var presenter: GroupPresenter

    private lateinit var map: GoogleMap
    @Inject lateinit var membersListAdapter: MembersListAdapter
    private val markers = hashMapOf<String, Marker>()
    private var mapReady = false
    private var alreadyLoaded = false

    private lateinit var lastLatLngBounds: LatLngBounds
    private var mapCentringInterrupted = false
    private var mapDuringMoving = false

    companion object {
        const val LOCATION_PERMISSION_REQUEST = 1

        fun createIntent(context: Context): Intent =
                Intent(context, GroupActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group)
        setSupportActionBar(toolbar)

        // Setup views
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        centerButton.setOnClickListener { centerMap() }
        membersListAdapter.onMemberClickListener = this::focusMemberOnMap
        membersListAdapter.onMemberPromoteListener = presenter::promoteMember
        membersListAdapter.onMemberSuppressListener = presenter::suppressMember
        membersListAdapter.onMemberBlockListener = presenter::blockMember
        membersList.apply {
            prepare()
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

            adapter = membersListAdapter
        }

        presenter.subscribe(this)

        // CoordsTrackerService stuff
        if (checkLocationPermissions()) {
            startTrackerService()
        }
    }

    private fun checkLocationPermissions(): Boolean {
        val coarsePermissionCheck = ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION)
        val finePermissionCheck = ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION)

        if (coarsePermissionCheck != PackageManager.PERMISSION_GRANTED
                || finePermissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
            ), LOCATION_PERMISSION_REQUEST)

            return false
        }

        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode != LOCATION_PERMISSION_REQUEST) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            return
        }

        if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
            startTrackerService()
        } else {
            presenter.leaveGroup()
            Toast.makeText(this, resources.getString(R.string.location_permission_not_granted), Toast.LENGTH_LONG).show()
        }
    }

    private fun startTrackerService() {
        CoordsTrackerService.start(this)
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
            presenter.showQr()
            true
        }
        R.id.action_leave -> {
            presenter.leaveGroup()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.uiSettings.isMapToolbarEnabled = false
        map.setOnCameraMoveListener {
            synchronized(mapDuringMoving) {
                if (!mapDuringMoving) {
                    interruptMapCentering()
                }
            }
        }

        mapReady = true

        presenter.loadMembers()
    }

    override fun showMembers(members: List<Member>) {
        if (!mapReady) {
            return
        }

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
                position = it.getLatLng()
                title = it.name

                boundsBuilder.include(position)
            }
        }

        markersToDelete.forEach {
            markers[it]?.remove()
            markers.remove(it)

            membersListAdapter.removeMember(it)
        }

        membersListAdapter.commitChanges()

        if (markers.size > 0) {
            // Move map's camera boundaries to have it containing all markers
            lastLatLngBounds = boundsBuilder.build()

            if (!alreadyLoaded) {
                map.moveCamera(
                        paddedLngBoundsCameraUpdate(lastLatLngBounds)
                )
            } else if (!mapCentringInterrupted) {
                mapDuringMoving = true
                map.animateCamera(
                        paddedLngBoundsCameraUpdate(lastLatLngBounds),
                        object : GoogleMap.CancelableCallback {
                            override fun onFinish() { synchronized(mapDuringMoving) { mapDuringMoving = false } }
                            override fun onCancel() = interruptMapCentering()
                        }
                )
            }
        }

        alreadyLoaded = true
    }

    override fun focusMemberOnMap(id: String) {
        markers[id]?.apply {
            if (markers.size == 1 && !mapCentringInterrupted) {
                // If map is already centered on the center of all members (on the only one),
                // we do not need to move camera.
                showInfoWindow()
                return@apply
            }

            interruptMapCentering()

            map.animateCamera(
                    CameraUpdateFactory.newLatLng(this.position),
                    object : GoogleMap.CancelableCallback {
                        override fun onFinish() = showInfoWindow()
                        override fun onCancel() = Unit
                    }
            )
        }
    }

    private fun interruptMapCentering() {
        centerButton.isVisible = true

        mapCentringInterrupted = true
        mapDuringMoving = false
    }

    private fun centerMap() {
        centerButton.isVisible = false

        mapCentringInterrupted = false
        mapDuringMoving = true
        map.animateCamera(
                paddedLngBoundsCameraUpdate(lastLatLngBounds),
                object : GoogleMap.CancelableCallback {
                    override fun onFinish() { synchronized(mapDuringMoving) { mapDuringMoving = false } }
                    override fun onCancel() = interruptMapCentering()
                }
        )
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

    override fun displayMemberBlockConfirmation(member: Member, callback: (Boolean) -> Unit) {
        AlertDialog.Builder(this)
                .setTitle(resources.getString(R.string.kick_member_modal_title))
                .setMessage(resources.getString(R.string.kick_member_modal_text, member.name))
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

    override fun informAboutBeingKicked() {
        Toast.makeText(this, "You have been kicked from group.", Toast.LENGTH_LONG).show()
    }
}