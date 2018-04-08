package me.wolszon.groupie.ui.landing.vision

import com.google.android.gms.vision.MultiProcessor
import com.google.android.gms.vision.Tracker
import com.google.android.gms.vision.barcode.Barcode

class BarcodeTrackerFactory : MultiProcessor.Factory<Barcode> {
    lateinit var barcodeDetectListener: (Barcode) -> Unit

    override fun create(barcode: Barcode): Tracker<Barcode> {
        return object : Tracker<Barcode>() {
            override fun onNewItem(id: Int, barcode: Barcode) {
                barcodeDetectListener(barcode)
            }
        }
    }
}