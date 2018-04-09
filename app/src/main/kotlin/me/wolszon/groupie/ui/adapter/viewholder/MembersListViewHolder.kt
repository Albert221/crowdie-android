package me.wolszon.groupie.ui.adapter.viewholder

import android.graphics.drawable.ColorDrawable
import android.support.v7.widget.RecyclerView
import android.view.View
import com.daimajia.swipe.SwipeLayout
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.members_list_item.*
import me.wolszon.groupie.R
import me.wolszon.groupie.api.models.dataclass.Member
import me.wolszon.groupie.isVisible
import me.wolszon.groupie.utils.DistanceFormatter

class MembersListViewHolder(override val containerView: View,
                            private val onMemberClickListener: (String) -> Unit,
                            private val onMemberPromoteListener: (String) -> Unit,
                            private val onMemberSuppressListener: (String) -> Unit,
                            private val onMemberBlockListener: (String) -> Unit) : RecyclerView.ViewHolder(containerView), LayoutContainer {
    fun bindView(member: Member) {
        // FIXME: This method is a mess
        (containerView as SwipeLayout).showMode = SwipeLayout.ShowMode.LayDown
        name.text = member.name

        if (member.role == Member.MEMBER) {
            promote.isVisible = true
            suppress.isVisible = false
            clickableArea.background = ColorDrawable(containerView.resources.getColor(android.R.color.background_light))
        } else if (member.role == Member.ADMIN) {
            promote.isVisible = false
            suppress.isVisible = true
            clickableArea.background = containerView.resources.getDrawable(R.drawable.admin_indicator)
        }

        if (member.isYou()) {
            showYouPill()
        } else {
            showDistancePill(member.distanceFromUser())
        }

        clickableArea.setOnClickListener { onMemberClickListener(member.id) }
        promote.setOnClickListener { onMemberPromoteListener(member.id) }
        suppress.setOnClickListener { onMemberSuppressListener(member.id) }
        block.setOnClickListener { onMemberBlockListener(member.id) }
    }

    private fun showYouPill() {
        status.text = containerView.resources.getString(R.string.you)
        status.setTextColor(containerView.resources.getColor(android.R.color.holo_blue_dark))
        status.background = containerView.resources.getDrawable(R.drawable.pill_you)
    }

    private fun showDistancePill(distance: Int) {
        status.text = DistanceFormatter.format(distance).toUpperCase()
        status.setTextColor(containerView.resources.getColor(android.R.color.holo_green_dark))
        status.background = containerView.resources.getDrawable(R.drawable.pill_distance)
    }
}