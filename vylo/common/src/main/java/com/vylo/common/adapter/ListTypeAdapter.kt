package com.vylo.common.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vylo.common.R

class ListTypeAdapter(private val mAdapterCallback: ListTypeAdapterCallback) : RecyclerView.Adapter<ListTypeAdapter.VH>() {

    private val data: MutableList<String> = mutableListOf()

    interface ListTypeAdapterCallback {
        fun clickOnItem(item: String?, isChooseItem: Boolean)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_list_type, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = data[position]
        holder.typeName.text = item
        holder.itemView.setOnClickListener {
            mAdapterCallback.clickOnItem(item, true)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val typeName: TextView = itemView.findViewById(R.id.type_name)
    }

    fun addData(data: MutableList<String>?) {
        if (data != null) {
            clearData()
            this.data.addAll(data)
        }
        notifyDataSetChanged()
    }

    fun clearData() {
        data.clear()
        notifyDataSetChanged()
    }

}