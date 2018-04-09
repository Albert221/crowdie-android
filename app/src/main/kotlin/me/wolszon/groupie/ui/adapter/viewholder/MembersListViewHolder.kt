package me.wolszon.groupie.ui.adapter.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import com.daimajia.swipe.SwipeLayout
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.members_list_item.*
import me.wolszon.groupie.GroupieApplication
import me.wolszon.groupie.api.models.dataclass.Member
import me.wolszon.groupie.isVisible

class MembersListViewHolder(override val containerView: View,
                            private val onMemberClickListener: (String) -> Unit,
                            private val onMemberPromoteListener: (String) -> Unit,
                            private val onMemberSuppressListener: (String) -> Unit,
                            private val onMemberBlockListener: (String) -> Unit) : RecyclerView.ViewHolder(containerView), LayoutContainer {
    fun bindView(member: Member) {
        (containerView as SwipeLayout).showMode = SwipeLayout.ShowMode.LayDown
        name.text = member.name

        if (member.role == Member.MEMBER) {
            promote.isVisible = true
            suppress.isVisible = false
        } else {
            promote.isVisible = false
            suppress.isVisible = true
        }

        status.text = when (member.androidId) {
            GroupieApplication.androidId -> "YOU"
            else -> "rotfl"
        }

        clickableArea.setOnClickListener { onMemberClickListener(member.id) }
        promote.setOnClickListener { onMemberPromoteListener(member.id) }
        suppress.setOnClickListener { onMemberSuppressListener(member.id) }
        block.setOnClickListener { onMemberBlockListener(member.id) }
    }
}