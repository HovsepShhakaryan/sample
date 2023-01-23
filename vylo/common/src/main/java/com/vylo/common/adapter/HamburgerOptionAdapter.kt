package com.vylo.common.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vylo.common.adapter.entity.KebabOption
import com.vylo.common.databinding.ItemHamburgerOptionBinding

class HamburgerOptionAdapter(
    private val list: List<KebabOption>
) : RecyclerView.Adapter<HamburgerOptionAdapter.HamburgerOptionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HamburgerOptionViewHolder {
        return HamburgerOptionViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: HamburgerOptionViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size

    class HamburgerOptionViewHolder(
        private val binding: ItemHamburgerOptionBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: KebabOption) {
            binding.apply {
                item.drawImage?.let {
                    image.setImageDrawable(it)
                }
                option.text = item.option
                item.color?.let {
                    option.setTextColor(it)
                }

                item.click?.let {
                    option.setOnClickListener { it() }
                    image.setOnClickListener { it() }
                }
            }
        }

        companion object {
            fun create(parent: ViewGroup): HamburgerOptionViewHolder {
                return LayoutInflater.from(parent.context).let {
                    ItemHamburgerOptionBinding.inflate(it, parent, false)
                }.let {
                    HamburgerOptionViewHolder(it)
                }
            }
        }
    }
}