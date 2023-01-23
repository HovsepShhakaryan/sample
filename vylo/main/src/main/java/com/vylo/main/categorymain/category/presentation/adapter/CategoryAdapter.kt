package com.vylo.main.categorymain.category.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vylo.common.entity.CommonCategoryItem
import com.vylo.main.databinding.ItemCategoryBinding

class CategoryAdapter(
    private val list: List<CommonCategoryItem>,
    private val onClick: (id: String, name: String) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder.create(parent, onClick)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size

    class CategoryViewHolder(
        private val binding: ItemCategoryBinding,
        private val onClick: (id: String, name: String) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CommonCategoryItem) {
            binding.apply {
                category.text = item.name
                category.setOnClickListener {
                    if (item.globalId != null && item.name != null)
                        onClick(item.globalId!!, item.name!!)
                }
            }
        }

        companion object {
            fun create(
                parent: ViewGroup,
                onClick: (id: String, name: String) -> Unit
            ): CategoryViewHolder {
                return LayoutInflater.from(parent.context).let {
                    CategoryViewHolder(ItemCategoryBinding.inflate(it, parent, false), onClick)
                }
            }
        }
    }
}