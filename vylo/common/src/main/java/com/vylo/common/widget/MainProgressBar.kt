package com.vylo.common.widget

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import com.vylo.common.R

class MainProgressBar(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
    LinearLayout(context!!, attrs, defStyleAttr) {

    private var activity: Activity? = null

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    init {
        inflate(context, R.layout.progress_bar, this)
        this.visibility = View.GONE
    }

    fun show() {
        this.visibility = View.VISIBLE
        if (activity != null)
            freezeTouch(activity!!)
    }

    fun hide() {
        this.visibility = View.GONE
        if (activity != null)
            unfreezeTouch(activity!!)
    }

    private fun freezeTouch(activity: Activity) {
        activity.window?.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    private fun unfreezeTouch(activity: Activity) {
        activity.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    fun setActivity(activity: Activity) {
        this.activity = activity
    }

    override fun onDetachedFromWindow() {
        if (activity != null)
            unfreezeTouch(activity!!)
        super.onDetachedFromWindow()
    }
}