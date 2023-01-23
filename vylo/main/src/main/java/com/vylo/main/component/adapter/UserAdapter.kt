package com.vylo.main.component.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.vylo.common.adapter.entity.UserInfo
import com.vylo.common.entity.PublishersSubscription
import com.vylo.main.databinding.ItemUserBinding

class UserAdapter(
    private val onClick: (Long, Boolean) -> Unit,
    private val onUserClick: (String?) -> Unit
) : RecyclerView.Adapter<UserAdapter.VyloViewHolder>(), Filterable {

    private val userList = mutableListOf<PublishersSubscription>()
    private val userListFull = mutableListOf<PublishersSubscription>()
    private var isRefresh = false
    private var data = mutableListOf<PublishersSubscription>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VyloViewHolder {
        return VyloViewHolder.create(parent, onClick, onUserClick)
    }

    override fun onBindViewHolder(holder: VyloViewHolder, position: Int) {
        holder.bind(userList[position])
    }

    override fun getItemCount() = userList.size

    fun submitList(list: List<PublishersSubscription>) {
        isRefresh = false
        userList.clear()
        userListFull.clear()
        userList.addAll(list)
        userListFull.addAll(list)
        notifyDataSetChanged()
    }

    fun refreshData() {
        isRefresh = true
        userList.clear()
        userListFull.clear()
        notifyDataSetChanged()
    }

    fun setFollowItem(id: Long, isFollow: Boolean) {
        if (userList.isNotEmpty()) {
            userList.filter { it.id == id }.firstOrNull()?.isFollow = isFollow
            userListFull.filter { it.id == id }.firstOrNull()?.isFollow = isFollow
        }
    }

    class VyloViewHolder(
        private val binding: ItemUserBinding,
        private val onClick: (Long, Boolean) -> Unit,
        private val onUserClick: (String?) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PublishersSubscription) {
            binding.apply {
                UserInfo(
                    id = item.id ?: 0,
                    globalId = item.globalId,
                    reporterImage = item.profilePhoto,
                    name = item.name.orEmpty(),
                    subName = item.getUserName(),
                    isVerified = item.isApproved ?: false,
                    isButtonFollowing = true,
                    isFollower = item.isFollow
                ).let {
                    userView.initialize(it, onClick, onUserClick)
                }
            }
        }

        companion object {
            fun create(
                parent: ViewGroup,
                onClick: (Long, Boolean) -> Unit,
                onUserClick: (String?) -> Unit
            ): VyloViewHolder {
                return LayoutInflater.from(parent.context).let {
                    VyloViewHolder(ItemUserBinding.inflate(it, parent, false), onClick, onUserClick)
                }
            }
        }
    }

    override fun getFilter() = filteredList

    private val filteredList = object : Filter() {

        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val resultList = mutableListOf<PublishersSubscription>()

            if (constraint.isNullOrEmpty()) {
                resultList.addAll(userListFull)
            } else {
                userListFull.forEach {
                    if (it.name?.contains(constraint, true) == true
                        || it.username?.contains(constraint, true) == true
                    ) {
                        resultList.add(it)
                    }
                }
            }

            data.clear()
            data.addAll(resultList)

            return FilterResults().apply { values = resultList }
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            if (!isRefresh) {
                results?.let {
                    userList.clear()
                    userList.addAll(it.values as List<PublishersSubscription>)
                    notifyDataSetChanged()
                }
            }
        }
    }

    fun clearData() {
        userList.clear()
    }

    fun getData() = userList
}