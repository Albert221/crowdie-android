package me.wolszon.groupie.ui.adapter

import android.os.Handler
import android.support.v7.widget.RecyclerView
import android.util.Log
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

    fun addMember(member: Member) {
        members.add(member)

        // FIXME: Method commented below result in items not showing in RecyclerView. STRANGE!
        // notifyItemInserted(members.indexOfFirst { it.id == member.id })

        notifyDataSetChanged()
    }

    fun updateMember(member: Member) {
        // Prevent updating unchanged items
        if (members.indexOfFirst { it == member } != -1) return

        members.indexOfFirst { it.id == member.id }.apply {
            members[this] = member
            notifyItemChanged(this)
        }
    }

    fun removeMember(memberId: String) {
        members.indexOfFirst { it.id == memberId }.apply {
            members.removeAt(this)
            notifyItemRemoved(this)
        }
    }
}