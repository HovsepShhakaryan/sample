package com.vylo.common.util

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class ScrollBottomListener(linearLayoutManager: LinearLayoutManager?): RecyclerView.OnScrollListener() {

    private var mIsSilent = false
    private var mIsEnd = false
    private var mLinearLayoutManager: LinearLayoutManager? = null

    init {
        mLinearLayoutManager = linearLayoutManager
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
    }


    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        if (mIsSilent || mIsEnd) return
        val totalItemCount = mLinearLayoutManager!!.itemCount
        val visibleItemCount = mLinearLayoutManager!!.childCount
        val firstVisibleItemPosition = mLinearLayoutManager!!.findFirstVisibleItemPosition()
        if (totalItemCount == firstVisibleItemPosition + visibleItemCount)
            mIsEnd = onScrolledToBottom()
    }


    abstract fun onScrolledToBottom(): Boolean

    fun isSilent(isSilenced: Boolean) { mIsSilent = isSilenced }

    fun getEnd() = mIsEnd

}
