package com.vylo.main.trending.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vylo.main.databinding.ItemTrendingBinding
import com.vylo.main.homefragment.domain.entity.response.FeedItem

class TrendingAdapter(
    private val onClick: (Int, Boolean) -> Unit
) : RecyclerView.Adapter<TrendingAdapter.TrendingViewHolder>() {

    private val trendingList = mutableListOf<FeedItem>()
    private var isRefresh = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrendingViewHolder {
        return TrendingViewHolder.create(parent, onClick)
    }

    override fun onBindViewHolder(holder: TrendingViewHolder, position: Int) {
        holder.bind(trendingList[position])
    }

    override fun getItemCount() = trendingList.size

    fun submitList(list: List<FeedItem>) {
        isRefresh = false
        trendingList.addAll(list)
//        notifyItemRangeInserted(index, list.size)
    }

    fun refreshData() {
        isRefresh = true
        trendingList.clear()
        notifyDataSetChanged()
    }

    class TrendingViewHolder(
        private val binding: ItemTrendingBinding,
        private val onClick: (Int, Boolean) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: FeedItem) {
            binding.apply {

            }
        }

        companion object {
            fun create(parent: ViewGroup, onClick: (Int, Boolean) -> Unit): TrendingViewHolder {
                return LayoutInflater.from(parent.context).let {
                    TrendingViewHolder(ItemTrendingBinding.inflate(it, parent, false), onClick)
                }
            }
        }
    }
}