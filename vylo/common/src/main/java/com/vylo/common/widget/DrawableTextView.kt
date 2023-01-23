package com.vylo.common.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.appcompat.widget.SwitchCompat
import androidx.constraintlayout.widget.ConstraintLayout
import com.vylo.common.R
import com.vylo.common.adapter.entity.DrawableTextItem
import com.vylo.common.ext.toVisible

class DrawableTextView(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
    LinearLayoutCompat(context!!, attrs, defStyleAttr) {

    private val container: ConstraintLayout
    private val startImage: ImageView
    private val startImageTextSpace: View
    private val startText: TextView
    private val endText: TextView
    private val endImage: ImageView
    private val switcher: SwitchCompat

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    fun initialize(item: DrawableTextItem) {
        startText.text = item.startText
        item.startTextColor?.let {
            startText.setTextColor(it)
        }
        item.endText?.let {
            endText.text = it
            endText.toVisible()
        }
        item.endTextColor?.let {
            endText.setTextColor(it)
        }
        item.startImage?.let {
            startImage.setImageDrawable(it)
            startImage.toVisible()
            startImageTextSpace.toVisible()
        }
        item.endImage?.let {
            endImage.setImageDrawable(it)
            endImage.toVisible()
        }
        item.isSwitch?.let {
            switcher.isChecked = it
            switcher.toVisible()
        }
        item.click?.let {
            container.setOnClickListener {
                item.isSwitch?.let {
                    item.isSwitch = !it
                    switcher.isChecked = item.isSwitch!!
                    it(item.isSwitch)
                } ?: run {
                    it(null)
                }
            }
        }
    }

    init {
        inflate(context, R.layout.drawable_text_view, this)

        container = findViewById(R.id.container)
        startImage = findViewById(R.id.start_image)
        startImageTextSpace = findViewById(R.id.start_image_text_space)
        startText = findViewById(R.id.start_text)
        endText = findViewById(R.id.end_text)
        endImage = findViewById(R.id.end_image)
        switcher = findViewById(R.id.switcher)
    }
}