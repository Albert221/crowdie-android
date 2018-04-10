package me.wolszon.groupie.android.ui.group

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.util.LruCache
import kotlinx.android.synthetic.main.activity_group_qr.*
import me.wolszon.groupie.R
import me.wolszon.groupie.base.BaseActivity
import net.glxn.qrgen.android.QRCode

class GroupQrActivity : BaseActivity() {
    private val groupId by lazy { intent.getStringExtra(EXTRA_GROUP_ID) }

    companion object {
        const val EXTRA_GROUP_ID = "EXTRA_GROUP_ID"

        fun createIntent(context: Context, groupId: String): Intent {
            return Intent(context, GroupQrActivity::class.java).apply {
                putExtra(EXTRA_GROUP_ID, groupId)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_qr)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        id.text = groupId
        qr.setImageBitmap(getCachedQrBitmap(groupId))
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