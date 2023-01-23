package com.vylo.main.followmain.followingfragment.presentation.adapter

import android.content.Context
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.vylo.common.R
import com.vylo.common.entity.PublishersSubscription
import com.vylo.main.databinding.ItemProfileBinding

class ProfileFollowAdapter(
    private val callBack: AdapterCallBack,
    private val navigateToProfile: NavigateToProfile,
    private val context: Context,
) : RecyclerView.Adapter<ProfileFollowAdapter.ProfileViewHolder>() {

    interface AdapterCallBack {
        fun followCategory(id: Long, isFollowing: Boolean?)
    }

    interface NavigateToProfile {
        fun navigateToProfile(id: String?)
    }

    private var data = mutableListOf<PublishersSubscription>()

    override fun getItemViewType(position: Int) = position

    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val binding = ItemProfileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProfileViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item, position)
    }

    inner class ProfileViewHolder(private val binding: ItemProfileBinding) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var profileItem: PublishersSubscription
        private var isFollowing: Boolean? = null

        fun bind(profileItem: PublishersSubscription, position: Int) {
            this.profileItem = profileItem

            profileItem.profilePhoto?.let {
                Picasso.get().load(profileItem.profilePhoto).into(binding.imageOfReporter)
            }

            isFollowing = !profileItem.isFollow
            setFollowButtonText(isFollowing, isChangeFollowing = false)

            val ssb = SpannableStringBuilder()
            ssb.append(profileItem.getUserName())
//            if (profileItem.isApproved != null && profileItem.isApproved!!) {
//                ssb.append("  ")
//                ssb.setSpan(ImageSpan(context, com.vylo.main.R.drawable.ic_verified_profile), ssb.length - 1, ssb.length, 0)
//            }
            binding.labelName.text = profileItem.name

            binding.labelSubname.text = profileItem.getUserName()

            binding.buttonFollowing.setOnClickListener {
                isFollowing = !isFollowing!!
                setFollowButtonText(isFollowing, true)
                profileItem.isFollow = true
                data[position] = profileItem.copy(
                    isFollow = isFollowing!!
                )
            }

            itemView.setOnClickListener {
                navigateToProfile.navigateToProfile(profileItem.globalId)
            }

        }

        private fun setFollowButtonText(isFollower: Boolean?, isChangeFollowing: Boolean) {
            when (isFollower) {
                true -> {
                    binding.buttonFollowing.squareRoundedGrayButtonStyle(
                        context,
                        context.getString(R.string.button_following)
                    )
                    if (isChangeFollowing)
                        callBack.followCategory(profileItem.id!!, isFollowing)
                }
                false -> {
                    binding.buttonFollowing.squareRoundedWhiteButtonStyle(
                        context,
                        context.getString(R.string.button_follow)
                    )
                    if (isChangeFollowing)
                        callBack.followCategory(profileItem.id!!, isFollowing)
                }
                else -> {}
            }
        }

    }

    fun setData(items: List<PublishersSubscription>) {
        data.addAll(items)
        notifyDataSetChanged()
    }

    fun getData() = data

    fun clearData() {
        data.clear()
        notifyDataSetChanged()
    }
}