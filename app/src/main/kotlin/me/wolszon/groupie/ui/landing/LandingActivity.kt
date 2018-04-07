package me.wolszon.groupie.ui.landing

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_landing.*
import me.wolszon.groupie.R
import me.wolszon.groupie.base.BaseActivity
import javax.inject.Inject

class LandingActivity : BaseActivity(), LandingView {
    @Inject lateinit var presenter: LandingPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)

        createGroupButton.setOnClickListener { presenter.createGroup() }
        joinGroupButton.setOnClickListener {
            presenter.joinExistingGroup(groupIdText.text.toString())
        }
    }
}