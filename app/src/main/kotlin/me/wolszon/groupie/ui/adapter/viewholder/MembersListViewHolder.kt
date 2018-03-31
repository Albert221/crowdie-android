package me.wolszon.groupie.ui.adapter.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.members_list_item.*
import me.wolszon.groupie.api.models.dataclass.Member

class MembersListViewHolder(override val containerView: View,
                            private val onMemberClickListener: (String) -> Unit,
                            private val onMemberPromoteListener: (String) -> Unit,
                            private val onMemberSuppressListener: (String) -> Unit) : RecyclerView.ViewHolder(containerView), LayoutContainer {
    fun bindView(member: Member) {
        clickableArea.setOnClickListener { onMemberClickListener(member.id) }
        promote.setOnClickListener { onMemberPromoteListener(member.id) }
        suppress.setOnClickListener { onMemberSuppressListener(member.id) }

        name.text = member.name

        if (member.role == Member.MEMBER) {
            promote.visibility = View.VISIBLE
            suppress.visibility = View.GONE
        } else {
            promote.visibility = View.GONE
            suppress.visibility = View.VISIBLE
        }
    }
}