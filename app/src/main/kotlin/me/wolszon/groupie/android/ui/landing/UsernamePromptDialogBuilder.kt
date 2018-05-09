package me.wolszon.groupie.android.ui.landing

import android.content.Context
import com.afollestad.materialdialogs.MaterialDialog
import me.wolszon.groupie.R

class UsernamePromptDialogBuilder(context: Context) : MaterialDialog.Builder(context) {
    private lateinit var enterCallback: (String) -> Boolean
    private var dismissible: Boolean = false
    private var valid = false

    init {
        title = context.resources.getString(R.string.username_prompt_title)
        cancelable = false
        canceledOnTouchOutside = false

        input(context.resources.getString(R.string.username_prompt_input_hint), null, {
            _, input ->

            // validation
            if (input.isEmpty()) {
                valid = false
                return@input
            } else {
                valid = true
            }

            enterCallback(input.toString())
        })

        dismissListener {
            if (!dismissible && !valid) {
                this.show()
            }
        }
    }

    fun setDismissible(dismissible: Boolean): UsernamePromptDialogBuilder {
        this.dismissible = dismissible
        canceledOnTouchOutside = dismissible
        return this
    }

    fun setEnterCallback(enterCallback: (String) -> Boolean): UsernamePromptDialogBuilder {
        this.enterCallback = enterCallback
        return this
    }
}