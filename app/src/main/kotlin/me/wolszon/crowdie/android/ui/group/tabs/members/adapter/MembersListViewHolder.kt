package me.wolszon.crowdie.android.ui.group.tabs.members.adapter

import android.graphics.drawable.ColorDrawable
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.members_list_item.*
import me.wolszon.crowdie.R
import me.wolszon.crowdie.api.domain.GroupManager
import me.wolszon.crowdie.api.models.dataclass.Member
import me.wolszon.crowdie.utils.DistanceFormatter
import me.wolszon.crowdie.utils.isVisible


class MembersListViewHolder(override val containerView: View,
                            private val memberClickEventSubject: MemberClickEventSubject,
                            private val onMemberPromoteListener: (String) -> Unit,
                            private val onMemberSuppressListener: (String) -> Unit,
                            private val onMemberBlockListener: (String) -> Unit) : RecyclerView.ViewHolder(containerView), LayoutContainer {
    private lateinit var memberId: String
    private lateinit var memberRole: Member.Role

    fun bindView(member: Member) {
        // FIXME: This method is a mess
        memberId = member.id

        actionsButton.isVisible = GroupManager.state?.isAdmin() ?: false

        memberRole = member.role
        when (member.role) {
            Member.Role.MEMBER -> {
                clickableArea.background = ColorDrawable(
                        containerView.resources.getColor(android.R.color.background_light))
            }

            Member.Role.ADMIN -> {
                clickableArea.background = containerView.resources.getDrawable(
                        R.drawable.admin_indicator)
            }
        }

        if (member.isYou()) {
            showYouPill()
        } else {
            showDistancePill(member.distanceFromUser())
        }

        name.text = member.name
        clickableArea.setOnClickListener { memberClickEventSubject.onNext(member.id) }
        actionsButton.setOnClickListener { onActionsButtonClick(it) }
    }

    private fun showYouPill() {
        status.text = containerView.resources.getString(R.string.you)
        status.setTextColor(containerView.resources.getColor(R.color.textOnSecondary))
        status.background = containerView.resources.getDrawable(R.drawable.pill_you)
    }

    private fun showDistancePill(distance: Int) {
        status.text = DistanceFormatter.format(distance).toUpperCase()
        status.setTextColor(containerView.resources.getColor(R.color.textOnPrimary))
        status.background = containerView.resources.getDrawable(R.drawable.pill_distance)
    }

    private fun onActionsButtonClick(view: View) {
        val popup = PopupMenu(containerView.context, view)

        popup.menuInflater.inflate(R.menu.member_actions_menu, popup.menu)
        popup.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.suppress_action -> onMemberSuppressListener(memberId)
                R.id.promote_action -> onMemberPromoteListener(memberId)
                R.id.kick_action -> onMemberBlockListener(memberId)
                else -> return@setOnMenuItemClickListener false
            }

            true
        }

        popup.menu.apply {
            if (memberRole == Member.Role.MEMBER) {
                findItem(R.id.suppress_action).isVisible = false
                findItem(R.id.promote_action).isVisible = true
            } else {
                findItem(R.id.suppress_action).isVisible = true
                findItem(R.id.promote_action).isVisible = false
            }
        }

        popup.show()
    }
}