package com.vylo.main.component.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vylo.common.adapter.entity.CategoryInfo
import com.vylo.common.entity.CommonCategoryItem
import com.vylo.main.databinding.ItemCategoryViewBinding

class CategoryAdapter(
    private val onClick: (Long, Boolean) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    private val categoryList = mutableListOf<CommonCategoryItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder.create(parent, onClick)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(categoryList[position])
    }

    override fun getItemCount() = categoryList.size

    fun submitList(list: List<CommonCategoryItem>) {
        categoryList.clear()
        categoryList.addAll(list)
        notifyDataSetChanged()
    }

    fun refreshData() {
        categoryList.clear()
        notifyDataSetChanged()
    }

    fun setFollowItem(id: Long, isFollow: Boolean) {
        if (categoryList.isNotEmpty()) {
            categoryList.filter { it.id == id }.first().isFollow = isFollow
        }
    }

    fun updateFollowStatus(id: Long, isFollowing: Boolean) {
        categoryList.find { it.id == id }?.subCategories?.map { index ->
            categoryList.find { it.id == index }?.isFollow = isFollowing
            notifyItemChanged(categoryList.indexOfFirst { it.id == index })
        }
    }

    fun getSubCategoriesIds(id: Long) = categoryList.find { it.id == id }?.subCategories

    class CategoryViewHolder(
        private val binding: ItemCategoryViewBinding,
        private val onClick: (Long, Boolean) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CommonCategoryItem) {
            binding.apply {
                CategoryInfo(
                    id = item.id ?: 0,
                    globalId = item.globalId.orEmpty(),
                    name = item.name.orEmpty(),
                    isFollow = item.isFollow
                ).let {
                    categoryView.initialize(it, onClick, null, false)
                }
            }
        }

        companion object {
            fun create(parent: ViewGroup, onClick: (Long, Boolean) -> Unit): CategoryViewHolder {
                return LayoutInflater.from(parent.context).let {
                    CategoryViewHolder(
                        ItemCategoryViewBinding.inflate(it, parent, false),
                        onClick
                    )
                }
            }
        }
    }
}
