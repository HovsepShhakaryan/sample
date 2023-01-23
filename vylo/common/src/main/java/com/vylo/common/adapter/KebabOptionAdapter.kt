package com.vylo.common.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vylo.common.adapter.entity.KebabOption
import com.vylo.common.databinding.ItemKebabOptionBinding
import com.vylo.common.ext.drawableStart
import com.vylo.common.ext.orZero

class KebabOptionAdapter(
    private val list: List<KebabOption>
) : RecyclerView.Adapter<KebabOptionAdapter.KebabViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KebabViewHolder {
        return KebabViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: KebabViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size


    class KebabViewHolder(
        private val binding: ItemKebabOptionBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: KebabOption) {
            binding.apply {
                option.text = item.option
                item.color?.let {
                    option.setTextColor(it)
                }

                item.click?.let {
                    option.setOnClickListener {
                        if (item.imagesRes?.size.orZero() > 1) {
                            setImage(item.imagesRes, item.imagePaddingRes, item.isActive)
                        }
                        it()
                    }
                }

                item.clickWithParam?.let {
                    option.setOnClickListener {
                        item.isActive = !item.isActive
                        setImage(item.imagesRes, item.imagePaddingRes, item.isActive)
                        it(item.isActive)
                    }
                }

                setImage(item.imagesRes, item.imagePaddingRes, item.isActive)
            }
        }

        private fun setImage(images: List<Int>?, imagePaddingRes: Int?, isActive: Boolean) {
            images?.let {
                val img = if (!isActive) it.first() else it.last()
                binding.option.drawableStart(img, imagePaddingRes)
            }
        }

        companion object {
            fun create(parent: ViewGroup): KebabViewHolder {
                return LayoutInflater.from(parent.context).let {
                    ItemKebabOptionBinding.inflate(it, parent, false)
                }.let {
                    KebabViewHolder(it)
                }
            }
        }
    }
}