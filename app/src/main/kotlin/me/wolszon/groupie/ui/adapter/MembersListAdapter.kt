package me.wolszon.groupie.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import me.wolszon.groupie.R
import me.wolszon.groupie.api.models.dataclass.Member
import me.wolszon.groupie.inflate
import me.wolszon.groupie.ui.adapter.viewholder.MembersListViewHolder
import javax.inject.Inject

class MembersListAdapter @Inject() constructor() : RecyclerView.Adapter<MembersListViewHolder>() {
    val members = arrayListOf<Member>()

    lateinit var onMemberClickListener: (String) -> Unit
    lateinit var onMemberPromoteListener: (String) -> Unit
    lateinit var onMemberSuppressListener: (String) -> Unit
    lateinit var onMemberBlockListener: (String) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MembersListViewHolder =
            MembersListViewHolder(parent.inflate(R.layout.members_list_item),
                                  onMemberClickListener,
                                  onMemberPromoteListener,
                                  onMemberSuppressListener,
                                  onMemberBlockListener)

    override fun onBindViewHolder(holder: MembersListViewHolder?, position: Int) {
        holder?.apply {
            bindView(members[position])
        }
    }

    override fun getItemCount(): Int = members.count()

    fun showMembers(members: List<Member>) {
        this.members.apply {
            clear()
            addAll(members)
            notifyDataSetChanged()
        }
    }

    fun updateMember(member: Member) {
        members.apply {
            val index = indexOfFirst { it.id == member.id }
            set(index, member)
            notifyItemChanged(index)
        }
    }

    fun removeMember(member: Member) {
        members.indexOf(member).apply {
            members.removeAt(this)
            notifyItemRemoved(this)
        }
    }
}