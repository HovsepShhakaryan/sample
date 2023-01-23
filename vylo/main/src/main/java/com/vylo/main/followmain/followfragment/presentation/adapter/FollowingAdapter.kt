package com.vylo.main.followmain.followfragment.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.vylo.common.adapter.entity.UserInfo
import com.vylo.common.ext.orFalse
import com.vylo.main.component.entity.PublishersSubscription
import com.vylo.main.databinding.ItemFollowingBinding

class FollowingAdapter(
    private val onClick: (Long, Boolean) -> Unit,
    private val onUserClick: (String?) -> Unit
) : RecyclerView.Adapter<FollowingAdapter.FollowingViewHolder>(), Filterable {

    private val followingsList = mutableListOf<PublishersSubscription>()
    private val followingsListFull = mutableListOf<PublishersSubscription>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowingViewHolder {
        return FollowingViewHolder.create(parent, onClick, onUserClick)
    }

    override fun onBindViewHolder(holder: FollowingViewHolder, position: Int) {
        holder.bind(followingsList[position])
    }

    override fun getItemCount() = followingsList.size

    fun submitList(list: List<PublishersSubscription>) {
//        followingsList.clear()
//        followingsListFull.clear()
//        notifyDataSetChanged()

        val index = followingsList.size - 1
        followingsList.addAll(list)
//        followingsListFull.addAll(list)
        notifyItemRangeInserted(index, list.size)
    }

    fun clearData() {
        followingsList.clear()
        followingsListFull.clear()
        notifyDataSetChanged()
    }

    class FollowingViewHolder(
        private val binding: ItemFollowingBinding,
        private val onClick: (Long, Boolean) -> Unit,
        private val onUserClick: (String?) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PublishersSubscription) {
            binding.apply {
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
        }

        companion object {
            fun create(
                parent: ViewGroup,
                onClick: (Long, Boolean) -> Unit,
                onUserClick: (String?) -> Unit
            ): FollowingViewHolder {
                return LayoutInflater.from(parent.context).let {
                    FollowingViewHolder(
                        ItemFollowingBinding.inflate(it, parent, false),
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
                resultList.addAll(followingsListFull)
            } else {
                followingsListFull.forEach {
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
            results?.let {
                followingsList.clear()
                followingsList.addAll(it.values as List<PublishersSubscription>)
                notifyDataSetChanged()
            }
        }
    }
}