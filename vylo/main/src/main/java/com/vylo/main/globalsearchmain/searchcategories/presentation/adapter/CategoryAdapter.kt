package com.vylo.main.globalsearchmain.searchcategories.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vylo.common.R
import com.vylo.common.databinding.ItemUserViewBinding
import com.vylo.common.entity.CommonCategoryItem

class CategoryAdapter(
    adapterCallBack: AdapterCallBack,
    private val context: Context
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    interface AdapterCallBack {
        fun followCategory(id: Long, isFollowing: Boolean?)
        fun showHide(isShow: Boolean)
        fun getCategory()
    }

    private var searchIntoActialData = false
    private val callBack = adapterCallBack
    private lateinit var categoryAdapter: CategoryAdapter
    private var data = mutableListOf<CommonCategoryItem>()
    private var searchData = mutableListOf<CommonCategoryItem>()

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding =
            ItemUserViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item, position)
    }

    inner class CategoryViewHolder(private val binding: ItemUserViewBinding) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var searchCategoryItem: CommonCategoryItem
        private var isFollow: Boolean? = null
        private var position: Int? = 0

        fun bind(searchCategoryItem: CommonCategoryItem, position: Int) {
            this.searchCategoryItem = searchCategoryItem
            this.position = position

            isFollow = searchCategoryItem.isFollow

            binding.categoryTitle.text = searchCategoryItem.name
//            isFollow = false
            setFollowButtonText(isFollow, false)

            binding.buttonFollowing.setOnClickListener {
//                isFollow = !isFollow!!
                setFollowButtonText(isFollow, true)
                searchCategoryItem.isFollow = true
                data[position] = searchCategoryItem.copy(
                    isFollow = isFollow!!
                )
            }

            itemView.setOnClickListener {
                callBack.getCategory()
            }
        }

        private fun setFollowButtonText(isFollower: Boolean?, isChangeStatus: Boolean) {
            when (isFollower) {
                true -> {
                    isFollow = false
                    binding.buttonFollowing.squareRoundedGrayButtonStyle(
                        context,
                        context.getString(R.string.button_following)
                    )
                    if (isChangeStatus)
                        callBack.followCategory(searchCategoryItem.id!!, isFollower)
                }
                false -> {
                    isFollow = true
                    binding.buttonFollowing.squareRoundedWhiteButtonStyle(
                        context,
                        context.getString(R.string.button_follow)
                    )
                    if (isChangeStatus)
                        callBack.followCategory(searchCategoryItem.id!!, isFollower)
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

    fun setData(items: List<CommonCategoryItem>) {
        data.addAll(items)
        if (searchData.size > 0) searchData.clear()
        searchData.addAll(items)
        notifyDataSetChanged()
    }

    fun getData() = data

    fun clearData() {
        data.clear()
        searchData.clear()
        notifyDataSetChanged()
    }

    fun setActualData(items: List<CommonCategoryItem>) {
        if (searchData.size > 0) searchData.clear()
        searchData.addAll(items)
    }

    fun getSecondAdapter(categoryAdapter: CategoryAdapter) {
        this.categoryAdapter = categoryAdapter
    }

    fun setFollowItem(id: Long, isFollow: Boolean) {
        if (data.isNotEmpty()) {
            data.firstOrNull { it.id == id }?.isFollow = isFollow
            notifyItemChanged(data.indexOfFirst { it.id == id })
        }
    }

    fun updateFollowStatus(id: Long, isFollowing: Boolean) {
        data.find { it.id == id }?.subCategories?.map { index ->
            data.find { it.id == index }?.isFollow = isFollowing
            notifyItemChanged(data.indexOfFirst { it.id == index })
        }
    }

    fun updateFollowStatus(ids: List<Long?>?, isFollowing: Boolean) {
        ids?.map { index ->
            data.find { it.id == index }?.isFollow = isFollowing
            notifyItemChanged(data.indexOfFirst { it.id == index })
        }
    }

    fun getSubCategoriesIds(id: Long) = data.find { it.id == id }?.subCategories

    fun search(text: String) {
        val resultList = mutableListOf<CommonCategoryItem>()
        searchData.forEach {
            if (it.name?.lowercase()!!.contains(text.lowercase())) {
                resultList.add(it)
            }
        }

        if (categoryAdapter.searchData.size > 0) categoryAdapter.clearData()
        if (text.isNotEmpty()) categoryAdapter.setData(resultList)
        if (categoryAdapter.searchData.size > 0) callBack.showHide(true)
        else callBack.showHide(false)
    }
}