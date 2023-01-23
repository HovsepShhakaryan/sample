package com.vylo.main.globalsearchmain.searchprofiles.presentation.adapter

import android.content.Context
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.vylo.common.R
import com.vylo.main.databinding.ItemProfileBinding
import com.vylo.main.globalsearchmain.searchprofiles.domain.entity.response.ProfileItem

class ProfileAdapter(
    adapterCallBack: AdapterCallBack,
    private val context: Context,
) : RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder>() {

    interface AdapterCallBack {
        fun followCategory(id: Long, isFollowing: Boolean?)
        fun getProfile()
    }

    interface NavigateToProfile {
        fun navigateToProfile(id: String?)
    }

    private val callBack = adapterCallBack
    private var data = mutableListOf<ProfileItem>()

    companion object {
        private var navigateToProfile: NavigateToProfile? = null

        fun setListenerToNavigation(navigateToProfile: NavigateToProfile) {
            Companion.navigateToProfile = navigateToProfile
        }
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val binding = ItemProfileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProfileViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }

    inner class ProfileViewHolder(private val binding: ItemProfileBinding) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var profileItem: ProfileItem
        private var isFollow: Boolean? = null

        fun bind(profileItem: ProfileItem) {
            this.profileItem = profileItem

            isFollow = profileItem.isFollow

            profileItem.profilePhoto?.let {
                Picasso.get().load(profileItem.profilePhoto).into(binding.imageOfReporter)
            }

            setFollowButtonText(isFollow, false)

            val ssb = SpannableStringBuilder()
            ssb.append(profileItem.getUserNameValue())
//            ssb.append("  ")
//            if (profileItem.isApproved != null && profileItem.isApproved)
//                ssb.setSpan(ImageSpan(context, com.vylo.main.R.drawable.ic_verified_profile), ssb.length - 1, ssb.length, 0)
            binding.labelName.text = profileItem.name

            binding.labelSubname.text = ssb

            binding.buttonFollowing.setOnClickListener {
                setFollowButtonText(isFollow, true)
            }

            itemView.setOnClickListener {
                callBack.getProfile()
                navigateToProfile?.navigateToProfile(profileItem.globalId)
            }

        }

        private fun setFollowButtonText(isFollower: Boolean?, isChangeStatus: Boolean) {
            when (isFollower) {
                true -> {
                    isFollow = false
                    setButtonFollowingStyle(R.drawable.shape_white_border)
                    setFollowTextStyle(R.style.MainText_H8_1)
                    binding.buttonFollowing.setTitle(context.getString(R.string.button_following))
                    if (isChangeStatus)
                        callBack.followCategory(profileItem.id!!, isFollower)
                }
                false -> {
                    isFollow = true
                    setButtonFollowingStyle(R.drawable.shape_white_without_border)
                    setFollowTextStyle(R.style.MainText_H8_1_Dark)
                    binding.buttonFollowing.setTitle(context.getString(R.string.button_follow))
                    if (isChangeStatus)
                        callBack.followCategory(profileItem.id!!, isFollower)
                }
                else -> {}
            }
        }

        private fun setFollowTextStyle(resId: Int) {
            binding.buttonFollowing.setStyle(resId)
        }

        private fun setButtonFollowingStyle(resId: Int) {
            binding.buttonFollowing.setShape(resId)
        }
    }

    fun setData(items: List<ProfileItem>) {
        data.addAll(items)
        notifyDataSetChanged()
    }

    fun getData() = data

    fun clearData() {
        data.clear()
        notifyDataSetChanged()
    }
}