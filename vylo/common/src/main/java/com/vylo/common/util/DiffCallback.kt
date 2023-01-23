package com.vylo.common.util

import androidx.recyclerview.widget.DiffUtil

class DiffCallback(private val oldLoadList: List<Any>, private val newLoadList: List<Any>) : DiffUtil.Callback() {

    override fun getOldListSize() = oldLoadList.size

    override fun getNewListSize() = newLoadList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldLoadList[oldItemPosition] === newLoadList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldLoadList[oldItemPosition] == newLoadList[newItemPosition]
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return super.getChangePayload(oldItemPosition, newItemPosition)
    }

}