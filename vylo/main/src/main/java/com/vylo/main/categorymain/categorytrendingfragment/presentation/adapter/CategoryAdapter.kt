package com.vylo.main.categorymain.categorytrendingfragment.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vylo.common.adapter.entity.CategoryInfo
import com.vylo.common.entity.CommonCategoryItem
import com.vylo.common.ext.orZero
import com.vylo.main.databinding.ItemCategoryHolderBinding

class CategoryAdapter(
    private val onFollowClick: (Long, Boolean) -> Unit,
    private val onCategoryClick: (CommonCategoryItem) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    private val categoryList = mutableListOf<CommonCategoryItem>()
    private var isRefresh = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder.create(parent, onFollowClick, onCategoryClick)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(categoryList[position])
    }

    override fun getItemCount() = categoryList.size

    fun setFollowItem(id: Long, isFollow: Boolean) {
        if (categoryList.isNotEmpty()) {
            categoryList.filter { it.id == id }.first().isFollow = isFollow
            notifyDataSetChanged()
        }
    }

    fun updateFollowStatus(id: Long, isFollowing: Boolean) {
        categoryList.find { it.id == id }?.subCategories?.map { index ->
            categoryList.find { it.id == index }?.isFollow = isFollowing
            notifyItemChanged(categoryList.indexOfFirst { it.id == index })
        }
    }

    fun submitList(list: List<CommonCategoryItem>) {
        isRefresh = false
        categoryList.addAll(list)
        notifyDataSetChanged()
    }

    fun refreshData() {
        isRefresh = true
        categoryList.clear()
        notifyDataSetChanged()
    }

    fun getSubCategoriesIds(id: Long) = categoryList.find { it.id == id }?.subCategories

    class CategoryViewHolder(
        private val binding: ItemCategoryHolderBinding,
        private val onFollowClick: (Long, Boolean) -> Unit,
        private val onCategoryClick: (CommonCategoryItem) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CommonCategoryItem) {
            binding.apply {
                CategoryInfo(
                    id = item.id.orZero(),
                    globalId = item.globalId.orEmpty(),
                    name = item.name.orEmpty(),
                    subName = "21.2M Subscribers",
                    isButtonFollowing = true,
                    isFollow = item.isFollow,
                ).also {
                    userView.initialize(it, onFollowClick, onCategoryClick, true)
                }
            }
        }

        companion object {
            fun create(
                parent: ViewGroup,
                onFollowClick: (Long, Boolean) -> Unit,
                onCategoryClick: (CommonCategoryItem) -> Unit
            ): CategoryViewHolder {
                return LayoutInflater.from(parent.context).let {
                    CategoryViewHolder(
                        ItemCategoryHolderBinding.inflate(it, parent, false),
                        onFollowClick,
                        onCategoryClick
                    )
                }
            }
        }
    }
}