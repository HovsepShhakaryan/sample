package com.vylo.main.globalsearchmain.searchstartscreen.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vylo.main.databinding.ItemRecentTrendingBinding

class RecentTrendingAdapter(
    adapterCallBack: AdapterCallBack,
    private val context: Context,
) : RecyclerView.Adapter<RecentTrendingAdapter.RecentTrendingViewHolder>() {

    interface AdapterCallBack {
        fun getSearchedText(text: String)
    }

    private val callBack = adapterCallBack
    private var data = mutableListOf<String>()

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentTrendingViewHolder {
        val binding = ItemRecentTrendingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecentTrendingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecentTrendingAdapter.RecentTrendingViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }

    override fun getItemCount() = data.size

    inner class RecentTrendingViewHolder(private val binding: ItemRecentTrendingBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(recentTrendingItem: String) {
            binding.labelName.text = recentTrendingItem

            itemView.setOnClickListener {
                callBack.getSearchedText(recentTrendingItem)
            }
        }

    }

    fun setData(loads: List<String>) {
        data.addAll(loads)
        notifyDataSetChanged()
    }

    fun getData() = data

    fun clearData() {
        data.clear()
        notifyDataSetChanged()
    }
}