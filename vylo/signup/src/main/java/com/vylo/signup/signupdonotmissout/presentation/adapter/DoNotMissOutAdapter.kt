package com.vylo.signup.signupdonotmissout.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.vylo.common.util.enums.ButtonStyle
import com.vylo.signup.databinding.ItemDoNotMissOutBinding
import com.vylo.signup.signupdonotmissout.domain.entity.response.AutoFollowItem

class DoNotMissOutAdapter(
    private val onFollowClick: (String, Boolean) -> Unit
) : RecyclerView.Adapter<DoNotMissOutAdapter.DoNotMissOutViewHolder>() {

    private val list = mutableListOf<AutoFollowItem>()

    fun submitList(list: List<AutoFollowItem>) {
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    class DoNotMissOutViewHolder(
        private val binding: ItemDoNotMissOutBinding,
        private val onFollowClick: (String, Boolean) -> Unit,
        private val context: Context
    ) : RecyclerView.ViewHolder(binding.root) {

        private var isFollow = true

        fun bind(item: AutoFollowItem) {
            binding.apply {
                Picasso.get().load(item.profilePhoto).into(avatar)

                name.text = item.name
                subname.text = item.getUserName()
                description.text = item.description

                setFollowButtonText(isFollow)

                buttonFollowing.setOnClickListener {
                    isFollow = !isFollow
                    setFollowButtonText(isFollow)
                    onFollowClick(item.id.orEmpty(), isFollow)
                }
            }
        }

        private fun setFollowButtonText(isFollower: Boolean) {
            when (isFollower) {
                false -> {
                    binding.buttonFollowing.roundedWhiteButtonStyle(
                        context,
                        context.getString(com.vylo.common.R.string.button_follow),
                        ButtonStyle.ROUNDED_SMALL
                    )
                }
                true -> {
                    binding.buttonFollowing.roundedGrayWhiteTextButtonStyle(
                        context,
                        context.getString(com.vylo.common.R.string.button_following),
                        ButtonStyle.ROUNDED_SMALL
                    )
                }
            }
        }

        companion object {
            fun create(
                parent: ViewGroup,
                onFollowClick: (String, Boolean) -> Unit
            ) = DoNotMissOutViewHolder(
                ItemDoNotMissOutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                onFollowClick,
                parent.context
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoNotMissOutViewHolder {
        return DoNotMissOutViewHolder.create(parent, onFollowClick)
    }

    override fun onBindViewHolder(holder: DoNotMissOutViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size
}