package com.vylo.common.adapter

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vylo.common.R
import com.vylo.common.adapter.entity.SubcategoryInfo
import com.vylo.common.databinding.ItemUserViewBinding

class CategoriesAdapter : RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder>() {

    private val subcategoryList = mutableListOf<SubcategoryInfo>()
    private var onClick: ((Long, Boolean) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        return CategoriesViewHolder.create(parent, onClick)
    }

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        holder.bind(subcategoryList[position])
    }

    override fun getItemCount() = subcategoryList.size

    fun submitList(list: List<SubcategoryInfo>) {
        subcategoryList.removeAll(subcategoryList)
        subcategoryList.addAll(list)
        notifyDataSetChanged()
    }

    fun setOnClickListener(click: ((Long, Boolean) -> Unit)?) {
        onClick = click
    }

    class CategoriesViewHolder(
        private val binding: ItemUserViewBinding,
        private val onClick: ((Long, Boolean) -> Unit)?,
        private val resources: Resources
    ) : RecyclerView.ViewHolder(binding.root) {

        private var isFollow: Boolean = false

        private fun setFollowButtonText(isFollower: Boolean) {
            binding.apply {
                if (isFollower) {
                    buttonFollowing.setShape(R.drawable.shape_white_border)
                    buttonFollowing.setStyle(R.style.MainText_H8_1)
                    buttonFollowing.setTitle(resources.getString(R.string.button_following))
                } else {
                    buttonFollowing.setShape(R.drawable.shape_white_without_border)
                    buttonFollowing.setStyle(R.style.MainText_H8_1_Dark)
                    buttonFollowing.setTitle(resources.getString(R.string.button_follow))
                }
            }
        }

        fun bind(item: SubcategoryInfo) {
            binding.apply {
                categoryTitle.text = item.name
//                categorySubtitle.text = "41.2M Subscribers"
                isFollow = item.isFollow
                setFollowButtonText(isFollow)
                onClick?.let {
                    buttonFollowing.setOnClickListener {
                        isFollow = !isFollow
                        setFollowButtonText(isFollow)
                        it(item.id, isFollow)
                    }
                }
            }
        }

        companion object {
            fun create(
                parent: ViewGroup,
                onClick: ((Long, Boolean) -> Unit)?
            ): CategoriesViewHolder {
                return LayoutInflater.from(parent.context).let {
                    CategoriesViewHolder(
                        ItemUserViewBinding.inflate(it, parent, false),
                        onClick,
                        parent.context.resources
                    )
                }
            }
        }
    }
}