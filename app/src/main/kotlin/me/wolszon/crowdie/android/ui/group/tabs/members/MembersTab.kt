package me.wolszon.crowdie.android.ui.group.tabs.members

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.group_tab_members.*
import me.wolszon.crowdie.R
import me.wolszon.crowdie.android.ui.group.tabs.members.adapter.MembersListAdapter
import me.wolszon.crowdie.android.ui.landing.LandingActivity
import me.wolszon.crowdie.api.models.dataclass.Member
import me.wolszon.crowdie.base.BaseFragment
import me.wolszon.crowdie.utils.prepare
import javax.inject.Inject

class MembersTab : BaseFragment(), MembersView {
    @Inject lateinit var presenter: MembersPresenter
    @Inject lateinit var membersListAdapter: MembersListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.group_tab_members, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        membersListAdapter.apply {
            onMemberClickListener = {}
            onMemberPromoteListener = presenter::promoteMember
            onMemberSuppressListener = presenter::suppressMember
            onMemberBlockListener = presenter::blockMember
        }

        membersList?.apply {
            prepare()
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

            adapter = membersListAdapter
        }

        leaveGroupButton.setOnClickListener { presenter.leaveGroup() }

        presenter.subscribe(this)
    }

    override fun onResume() {
        super.onResume()

        presenter.subscribe(this)
    }

    override fun onPause() {
        super.onPause()

        presenter.unsubscribe()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        presenter.unsubscribe()
    }

    override fun showMembers(members: List<Member>) {
        membersListAdapter.updateMembers(members)
    }

    override fun displayMemberBlockConfirmation(member: Member, callback: (Boolean) -> Unit) {
        AlertDialog.Builder(activity)
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

    override fun openLandingActivity() {
        // FIXME: Temporary solution, because Navigator can't be injected into MembersPresenter :(
        startActivity(Intent(context, LandingActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        })
    }
}