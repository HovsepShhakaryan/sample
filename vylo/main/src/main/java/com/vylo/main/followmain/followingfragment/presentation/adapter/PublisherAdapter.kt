package com.vylo.main.followmain.followingfragment.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vylo.common.adapter.entity.UserInfo
import com.vylo.common.entity.PublishersSubscription
import com.vylo.main.databinding.ItemUserBinding

class PublisherAdapter(
    private val onClick: (Long, Boolean) -> Unit,
    private val onUserClick: (String?) -> Unit
) : RecyclerView.Adapter<PublisherAdapter.PublisherViewHolder>() {

    private val publisherList = mutableListOf<PublishersSubscription>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PublisherViewHolder {
        return PublisherViewHolder.create(parent, onClick, onUserClick)
    }

    override fun onBindViewHolder(holder: PublisherViewHolder, position: Int) {
        holder.bind(publisherList[position])
    }

    override fun getItemCount() = publisherList.size

    fun submitList(list: List<PublishersSubscription>) {
        publisherList.clear()
        publisherList.addAll(list)
        notifyDataSetChanged()
    }

    fun refreshData() {
        publisherList.clear()
        notifyDataSetChanged()
    }

    fun setFollowItem(id: Long, isFollow: Boolean) {
        if (publisherList.isNotEmpty()) {
            publisherList.filter { it.id == id }.first().isFollow = isFollow
        }
    }

    class PublisherViewHolder(
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
                    isFollower = true
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
            ): PublisherViewHolder {
                return LayoutInflater.from(parent.context).let {
                    PublisherViewHolder(
                        ItemUserBinding.inflate(it, parent, false),
                        onClick,
                        onUserClick
                    )
                }
            }
        }
    }
}