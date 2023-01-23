package com.vylo.main.followmain.followfragment.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.vylo.common.adapter.entity.UserInfo
import com.vylo.common.ext.orFalse
import com.vylo.main.component.entity.PublishersSubscription
import com.vylo.main.databinding.ItemFollowersBinding

class FollowersAdapter(
    private val onClick: (Long, Boolean) -> Unit,
    private val onUserClick: (String?) -> Unit
) : RecyclerView.Adapter<FollowersAdapter.FollowersViewHolder>(), Filterable {

    private val followersList = mutableListOf<PublishersSubscription>()
    private val followersListFull = mutableListOf<PublishersSubscription>()
    private var isRefresh = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowersViewHolder {
        return FollowersViewHolder.create(parent, onClick, onUserClick)
    }

    override fun onBindViewHolder(holder: FollowersViewHolder, position: Int) {
        holder.bind(followersList[position])
    }

    override fun getItemCount() = followersList.size

    fun submitList(list: List<PublishersSubscription>) {
        followersList.clear()
        followersListFull.clear()
        notifyDataSetChanged()

        isRefresh = false
        val index = followersListFull.size - 1
        followersList.addAll(list)
        followersListFull.addAll(list)
        notifyItemRangeInserted(index, list.size)
    }

    fun refreshData() {
        isRefresh = true
        followersList.clear()
        followersListFull.clear()
        notifyDataSetChanged()
    }

    class FollowersViewHolder(
        private val binding: ItemFollowersBinding,
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
                    isVerified = item.isApproved.orFalse(),
                    isButtonFollowing = true,
                    isFollower = item.imFollowing.orFalse()
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
            ): FollowersViewHolder {
                return LayoutInflater.from(parent.context).let {
                    FollowersViewHolder(
                        ItemFollowersBinding.inflate(it, parent, false),
                        onClick,
                        onUserClick
                    )
                }
            }
        }
    }

    override fun getFilter() = filteredList

    private val filteredList = object : Filter() {

        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val resultList = mutableListOf<PublishersSubscription>()

            if (constraint.isNullOrEmpty()) {
                resultList.addAll(followersListFull)
            } else {
                followersListFull.forEach {
                    if (it.name?.contains(constraint, true) == true
                        || it.username?.contains(constraint, true) == true
                    ) {
                        resultList.add(it)
                    }
                }
            }

            return FilterResults().apply { values = resultList }
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            if (!isRefresh) {
                results?.let {
                    followersList.clear()
                    followersList.addAll(it.values as List<PublishersSubscription>)
                    notifyDataSetChanged()
                }
            }
        }
    }
}