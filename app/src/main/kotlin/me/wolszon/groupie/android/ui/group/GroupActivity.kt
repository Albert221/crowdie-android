package me.wolszon.groupie.android.ui.group

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_group.*
import me.wolszon.groupie.R
import me.wolszon.groupie.android.services.CoordsTrackerService
import me.wolszon.groupie.base.BaseActivity
import me.wolszon.groupie.android.ui.group.tabs.MapTab
import me.wolszon.groupie.android.ui.group.tabs.MembersTab
import me.wolszon.groupie.android.ui.group.tabs.QrTab
import javax.inject.Inject

class GroupActivity : BaseActivity(), GroupView {
    @Inject lateinit var presenter: GroupPresenter

    private lateinit var tabs: Map<Tab, Fragment>

    enum class Tab {
        MAP, MEMBERS, QR, SETTINGS
    }

    companion object {
        const val LOCATION_PERMISSION_REQUEST = 1

        fun createIntent(context: Context): Intent =
                Intent(context, GroupActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group)

        setupTabs()

        setTab(Tab.MAP, saveToStack = false)

        navigation.setOnNavigationItemReselectedListener { /* Do nothing on purpose. */ }
        navigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.action_map -> setTab(Tab.MAP)
                R.id.action_members -> setTab(Tab.MEMBERS)
                R.id.action_qr -> setTab(Tab.QR)
                R.id.action_settings -> presenter.leaveGroup()

                else ->
                    return@setOnNavigationItemSelectedListener false
            }

            true
        }

        // CoordsTrackerService stuff
        if (checkLocationPermissions()) {
            startTrackerService()
        }

        presenter.subscribe(this)
    }

    private fun setupTabs() {
        val tabs = mutableMapOf<Tab, Fragment>(
                Tab.MAP to MapTab(),
                Tab.MEMBERS to MembersTab(),
                Tab.QR to QrTab()
        )

        tabs.forEach { (_, it) ->supportFragmentInjector().inject(it) }

        this.tabs = tabs.toMap()
    }

    private fun setTab(tabToSet: Tab, saveToStack: Boolean = true) {
        val tab = tabs[tabToSet]

        val transaction = supportFragmentManager
                .beginTransaction()
                .replace(R.id.tab_frame, tab)

        if (saveToStack) {
            transaction.addToBackStack(null)
        }

        transaction.commit()
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

    override fun informAboutBeingKicked() {
        Toast.makeText(this, "You have been kicked from group.", Toast.LENGTH_LONG).show()
    }
}