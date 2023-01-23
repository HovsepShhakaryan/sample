package com.vylo.signup.signupchoosecategory.presentation.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.vylo.signup.R
import com.vylo.signup.databinding.ItemChipBinding
import com.vylo.signup.signupchoosecategory.domain.entity.ChipList


class CategoryChipAdapter(
    adapterCallBack: AdapterCallBack,
    private val context: Context,
) : RecyclerView.Adapter<CategoryChipAdapter.ChipViewHolder>() {

    interface AdapterCallBack {
        fun onChipClicked(id: Long, isChecked: Boolean)
        fun countCheckedChip(count: Int)
    }

    private val callBack = adapterCallBack
    private var data = mutableListOf<ChipList>()
    private var counter = 0

    companion object {
        const val WEIGHT = 1.0f
        const val MARGIN = 24
        const val MIN_HEIGHT = 100f
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChipViewHolder {
        val binding = ItemChipBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChipViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChipViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }

    inner class ChipViewHolder(private val binding: ItemChipBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(chipList: ChipList) {
            var isChipOneClicked = false
            var isChipTwoClicked = false

            if (chipList.chipsOne != null) {
                val chipOne = Chip(context)
                val a = chipList.chipsOne.name?.length!!/0.2
                val paramsOne = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    WEIGHT
                )
                chipOne.layoutParams = paramsOne

                chipOne.text = chipList.chipsOne.name
                chipOne.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.primary)))
                chipOne.chipBackgroundColor = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.black_disable))
                chipOne.chipMinHeight = MIN_HEIGHT
                binding.line.addView(chipOne)

                chipOne.setOnClickListener {
                    if (!isChipOneClicked) {
                        counter++
                        chipOne.chipBackgroundColor = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.secondary))
                        isChipOneClicked = true
                    } else {
                        counter--
                        chipOne.chipBackgroundColor = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.black_disable))
                        isChipOneClicked = false
                    }
                    callBack.countCheckedChip(counter)
                    callBack.onChipClicked(chipList.chipsOne.id!!, isChipOneClicked)
                }
            }


            if (chipList.chipsTwo != null) {
                val chipTwo = Chip(context)
                val a = chipList.chipsTwo.name?.length!!/0.2
                val paramsTwo = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    WEIGHT
                )
                paramsTwo.marginStart = MARGIN
                chipTwo.layoutParams = paramsTwo

                chipTwo.text = chipList.chipsTwo.name
                chipTwo.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.primary)))
                chipTwo.chipBackgroundColor = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.black_disable))
                chipTwo.chipMinHeight = MIN_HEIGHT
                binding.line.addView(chipTwo)

                chipTwo.setOnClickListener {
                    if (!isChipTwoClicked) {
                        counter++
                        chipTwo.chipBackgroundColor = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.secondary))
                        isChipTwoClicked = true
                    } else {
                        counter--
                        chipTwo.chipBackgroundColor = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.black_disable))
                        isChipTwoClicked = false
                    }
                    callBack.countCheckedChip(counter)
                    callBack.onChipClicked(chipList.chipsTwo.id!!, isChipTwoClicked)
                }
            }
        }

    }

    fun setData(items: List<ChipList>) {
        data.addAll(items)
        notifyDataSetChanged()
    }

    fun getData() = data

    fun clearData() {
        data.clear()
        notifyDataSetChanged()
    }
}