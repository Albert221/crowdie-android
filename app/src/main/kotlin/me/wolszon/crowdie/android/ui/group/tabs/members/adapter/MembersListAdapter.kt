package me.wolszon.crowdie.android.ui.group.tabs.members.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import me.wolszon.crowdie.R
import me.wolszon.crowdie.api.models.dataclass.Member
import me.wolszon.crowdie.utils.inflate
import javax.inject.Inject

class MembersListAdapter(private val memberClickEventSubject: MemberClickEventSubject)
        : RecyclerView.Adapter<MembersListViewHolder>() {
    private val members = arrayListOf<Member>()

    var onMemberPromoteListener: (String) -> Unit = {}
    var onMemberSuppressListener: (String) -> Unit = {}
    var onMemberBlockListener: (String) -> Unit = {}

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MembersListViewHolder =
            MembersListViewHolder(parent.inflate(R.layout.members_list_item),
                    memberClickEventSubject,
                    onMemberPromoteListener,
                    onMemberSuppressListener,
                    onMemberBlockListener)

    override fun onBindViewHolder(holder: MembersListViewHolder, position: Int) {
        holder.apply {
            bindView(members[position])
        }
    }

    override fun getItemCount(): Int = members.count()

    override fun getItemId(position: Int): Long = members[position].id.hashCode().toLong()

    fun updateMembers(members: List<Member>) {
        this.members.clear()
        this.members.addAll(members)

        notifyDataSetChanged()
    }
}