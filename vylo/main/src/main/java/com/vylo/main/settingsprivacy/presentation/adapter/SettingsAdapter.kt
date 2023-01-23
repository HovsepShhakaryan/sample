package com.vylo.main.settingsprivacy.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vylo.common.adapter.entity.DrawableTextItem
import com.vylo.main.databinding.ItemDrawableTextBinding

class SettingsAdapter : RecyclerView.Adapter<SettingsAdapter.SettingsViewHolder>() {

    private val list = mutableListOf<DrawableTextItem>()

    class SettingsViewHolder(
        private val binding: ItemDrawableTextBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DrawableTextItem) {
            binding.apply {
                drawableText.initialize(item)
            }
        }

        companion object {
            fun create(parent: ViewGroup) = LayoutInflater.from(parent.context).let {
                ItemDrawableTextBinding.inflate(it, parent, false)
            }.let {
                SettingsViewHolder(it)
            }
        }
    }

    fun submitList(listData: List<DrawableTextItem>) {
        list.clear()
        list.addAll(listData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingsViewHolder {
        return SettingsViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: SettingsViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size
}