package me.wolszon.crowdie.android.ui.group.tabs.qr

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.util.LruCache
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.group_tab_qr.*
import me.wolszon.crowdie.R
import me.wolszon.crowdie.android.ui.group.tabs.qr.QrPresenter
import me.wolszon.crowdie.base.BaseFragment
import me.wolszon.crowdie.base.BaseView
import net.glxn.qrgen.android.QRCode
import javax.inject.Inject

class QrTab : BaseFragment(), QrView {
    @Inject lateinit var presenter: QrPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.group_tab_qr, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        groupCode.setOnClickListener {
            val clipboardManager = activity!!.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboardManager.primaryClip = ClipData.newPlainText("Group id", (it as TextView).text)

            Toast.makeText(activity, "Group id has been copied", Toast.LENGTH_SHORT).show()
        }

        presenter.subscribe(this)

        presenter
                .getGroupId()
                .subscribe {
                    groupCode.text = it
                    qr.setImageBitmap(getCachedQrBitmap(it))
                }
    }

    private fun getCachedQrBitmap(groupId: String): Bitmap {
        val cache = LruCache<String, Bitmap>(1 * 1024 * 1024)

        synchronized(cache) {
            if (cache.get(groupId) == null) {
                val bitmap = QRCode
                        .from(groupId)
                        .withSize(500, 500)
                        .withColor(Color.BLACK, Color.TRANSPARENT)
                        .bitmap()

                cache.put(groupId, bitmap)
            }
        }

        return cache.get(groupId)
    }
}