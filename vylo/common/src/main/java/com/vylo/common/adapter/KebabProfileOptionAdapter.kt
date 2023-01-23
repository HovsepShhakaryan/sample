package com.vylo.common.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vylo.common.adapter.entity.KebabOption
import com.vylo.common.databinding.ItemProfileKebabOptionBinding
import com.vylo.common.ext.drawableStart

class KebabProfileOptionAdapter(
    private val list: List<KebabOption>
) : RecyclerView.Adapter<KebabProfileOptionAdapter.KebabProfileViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KebabProfileViewHolder {
        return KebabProfileViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: KebabProfileViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size


    class KebabProfileViewHolder(
        private val binding: ItemProfileKebabOptionBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: KebabOption) {
            binding.apply {
                option.text = item.option
                item.click?.let {
                    option.setOnClickListener { it() }
                }

                item.clickWithParam?.let {
                    option.setOnClickListener { it(null) }
                }

                setImage(item.imagesRes, item.imagePaddingRes, item.isActive)

                item.color?.let {
                    option.setTextColor(it)
                }
            }
        }

        private fun setImage(images: List<Int>?, imagePaddingRes: Int?, isActive: Boolean) {
            images?.let {
                val img = if (isActive) it.first() else it.last()
                binding.option.drawableStart(img, imagePaddingRes)
            }
        }

        companion object {
            fun create(parent: ViewGroup): KebabProfileViewHolder {
                return LayoutInflater.from(parent.context).let {
                    ItemProfileKebabOptionBinding.inflate(it, parent, false)
                }.let {
                    KebabProfileViewHolder(it)
                }
            }
        }
    }
}