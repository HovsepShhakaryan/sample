package com.vylo.signup.signupinputdategender.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vylo.signup.R
import com.vylo.signup.databinding.ItemGenderBinding
import com.vylo.signup.signupinputdategender.domain.entity.GenderInner

class GenderAdapter(
    adapterCallBack: AdapterCallBack,
    private val context: Context,
) : RecyclerView.Adapter<GenderAdapter.GenderViewHolder>() {

    interface AdapterCallBack {
        fun itemClicked(genderItem: GenderInner)
    }

    private val callBack = adapterCallBack
    private var data = mutableListOf<GenderInner>()

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenderViewHolder {
        val binding = ItemGenderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GenderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GenderViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item, context)
    }

    override fun getItemCount() = data.size

    inner class GenderViewHolder(private val binding: ItemGenderBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(genderItem: GenderInner, context: Context) {

            binding.genderName.text = genderItem.genderName

            showHideCheck(genderItem.isChosen!!)

            itemView.setOnClickListener {
                callBack.itemClicked(genderItem)
            }
        }

        private fun showHideCheck(isChecked: Boolean) {
            when (isChecked) {
                true -> {
                    binding.actionIcon.visibility = View.VISIBLE
                    binding.genderName.setTextColor(context.getColor(R.color.primary))
                }
                else -> {
                    binding.actionIcon.visibility = View.INVISIBLE
                    binding.genderName.setTextColor(context.getColor(R.color.text))
                }
            }

        }
    }

    fun setData(loads: List<GenderInner>) {
        data.addAll(loads)
        notifyDataSetChanged()
    }

    fun getData() = data

    fun clearData() {
        data.clear()
        notifyDataSetChanged()
    }
}