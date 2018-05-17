package me.wolszon.groupie.android.ui.group.tabs.members

import android.app.AlertDialog
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.group_tab_members.*
import me.wolszon.groupie.R
import me.wolszon.groupie.android.ui.group.tabs.members.adapter.MembersListAdapter
import me.wolszon.groupie.api.models.dataclass.Member
import me.wolszon.groupie.base.BaseFragment
import me.wolszon.groupie.utils.prepare
import javax.inject.Inject

class MembersTab : BaseFragment(), MembersView {
    @Inject lateinit var presenter: MembersPresenter
    @Inject lateinit var membersListAdapter: MembersListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.group_tab_members, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        membersListAdapter.onMemberClickListener = {}
        membersListAdapter.onMemberPromoteListener = presenter::promoteMember
        membersListAdapter.onMemberSuppressListener = presenter::suppressMember
        membersListAdapter.onMemberBlockListener = presenter::blockMember
        membersList?.apply {
            prepare()
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

            adapter = membersListAdapter
        }

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
}