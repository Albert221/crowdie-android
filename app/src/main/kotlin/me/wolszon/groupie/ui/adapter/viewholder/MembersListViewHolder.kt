package me.wolszon.groupie.ui.adapter.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.members_list_item.view.*
import me.wolszon.groupie.api.models.dataclass.Member

class MembersListViewHolder(override val containerView: View,
                            val onMemberClickListener: (String) -> Unit) : RecyclerView.ViewHolder(containerView), LayoutContainer {
    fun bindView(member: Member) {
        containerView.name.text = member.name
        containerView.name.setOnClickListener {
            onMemberClickListener(member.id)
        }
    }
}