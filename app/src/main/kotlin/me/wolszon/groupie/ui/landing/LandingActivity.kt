package me.wolszon.groupie.ui.landing

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.view.SurfaceHolder
import android.widget.Toast
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.MultiProcessor
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import kotlinx.android.synthetic.main.activity_landing.*
import me.wolszon.groupie.R
import me.wolszon.groupie.base.BaseActivity
import me.wolszon.groupie.isVisible
import me.wolszon.groupie.ui.landing.vision.BarcodeTrackerFactory
import javax.inject.Inject

class LandingActivity : BaseActivity(), LandingView {
    @Inject lateinit var presenter: LandingPresenter

    private lateinit var cameraSource: CameraSource
    private lateinit var detector: Detector<Barcode>

    companion object {
        const val CAMERA_PERMISSION_REQUEST = 1
        const val BUNDLE_CAMERA_ALREADY_SHOWN = "CAMERA_ALREADY_SHOWN"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)

        createGroupButton.setOnClickListener { presenter.createGroup() }
        joinGroupButton.setOnClickListener {
            presenter.joinExistingGroup(groupIdText.text.toString())
        }

        if (savedInstanceState?.getBoolean(BUNDLE_CAMERA_ALREADY_SHOWN) == true) {
            cameraSurface.holder.addCallback(object : SurfaceHolder.Callback {
                override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) = createCameraSource()
                override fun surfaceDestroyed(holder: SurfaceHolder?) = Unit
                override fun surfaceCreated(holder: SurfaceHolder?) = Unit
            })
        }

        // Camera stuff
        cameraArea.setOnClickListener {
            if (!cameraTapText.isVisible) {
                return@setOnClickListener
            }

            val permissionCheck = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                createCameraSource()
            } else {
                requestCameraPermission()
            }
        }

        presenter.subscribe(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putBoolean(BUNDLE_CAMERA_ALREADY_SHOWN, !cameraTapText.isVisible)
    }

    private fun requestCameraPermission() =
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST)

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode != CAMERA_PERMISSION_REQUEST) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            return
        }

        if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            createCameraSource()
        } else {
            Toast.makeText(this, "Permission has not been granted", Toast.LENGTH_LONG).show()
        }
    }

    override fun onResume() {
        super.onResume()

        if (::cameraSource.isInitialized) {
            startCameraSource()
        }
    }

    override fun onPause() {
        super.onPause()

        if (::cameraSource.isInitialized) {
            cameraSource.stop()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        if (::cameraSource.isInitialized) {
            cameraSource.release()
        }
        presenter.unsubscribe()
    }

    private fun createCameraSource() {
        cameraTapText.isVisible = false

        detector = BarcodeDetector.Builder(applicationContext)
                .setBarcodeFormats(Barcode.QR_CODE)
                .build()
        val trackerFactory = BarcodeTrackerFactory()
        trackerFactory.barcodeDetectListener = this::onBarcodeDetection

        detector.setProcessor(MultiProcessor.Builder<Barcode>(trackerFactory).build())

        if (!detector.isOperational) {
            // That's okay, it's just to check if there's no storage so vision libs aren't going to be loaded.
            val filter = IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW)
            val hasLowStorage = registerReceiver(null, filter) != null

            if (hasLowStorage) {
                Toast.makeText(this, "You don't have free disk space for downloading vision libraries", Toast.LENGTH_LONG).show()
            }
        }

        cameraSource = CameraSource.Builder(applicationContext, detector)
                .setRequestedPreviewSize(cameraSurface.width, cameraSurface.height)
                .setAutoFocusEnabled(true)
                .build()

        startCameraSource()
    }

    private fun startCameraSource() {
        try {
            cameraSource.start(cameraSurface.holder)
        } catch (e: SecurityException) {
            cameraSource.release()
            e.printStackTrace()
        }
    }

    private fun onBarcodeDetection(barcode: Barcode) {
        presenter.joinExistingGroup(barcode.rawValue)
    }
}