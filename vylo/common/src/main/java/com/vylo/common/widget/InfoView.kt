package com.vylo.common.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import com.vylo.common.R

class InfoView(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
    LinearLayoutCompat(context!!, attrs, defStyleAttr) {

    private val keyInfo: TextView
    private val valueInfo: TextView

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    fun initialize(key: String, value: Int) {
        keyInfo.text = key
        valueInfo.text = value.toString()
    }

    init {
        inflate(context, R.layout.info_view, this)

        keyInfo = findViewById(R.id.key)
        valueInfo = findViewById(R.id.value)
    }
}