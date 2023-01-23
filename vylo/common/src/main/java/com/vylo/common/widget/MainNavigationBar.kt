package com.vylo.common.widget

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.vylo.common.R
import com.vylo.common.ext.*
import com.vylo.common.util.enums.DrawablePositionType
import com.vylo.common.util.enums.DrawablePositionType.*

class MainNavigationBar(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
    ConstraintLayout(context!!, attrs, defStyleAttr) {

    private val bottomBorder: View
    private val title: TextView
    private val buttonNext: TextView
    private val buttonBackText: TextView
    private val buttonBack: ImageView
    private val buttonMenu: ImageView
    private val titleImage: ImageView
    private val buttonBorderNext: MainButton

    fun setStyle(styleId: Int) {
        buttonNext.setTextAppearance(styleId)
    }

    fun showBottomBorder(isVisible: Int) {
        bottomBorder.visibility = isVisible
    }

    fun showButtonNext(isVisible: Int) {
        buttonNext.visibility = isVisible
    }

    fun showButtonBack(isVisible: Int) {
        buttonBack.visibility = isVisible
    }

    fun showButtonBackText(isVisible: Int) {
        buttonBackText.visibility = isVisible
    }

    fun showButtonMenu(isVisible: Int) {
        buttonMenu.visibility = isVisible
    }

    fun setButtonNextText(name: String) {
        buttonNext.text = name
    }

    fun setButtonBackText(name: String) {
        buttonBackText.text = name
    }

    fun setTitleFontFamily(titleFont: Typeface?) {
        title.typeface = titleFont
    }

    fun setTitle(
        titleName: String,
        drawableRes: Int? = null,
        positionType: DrawablePositionType? = null,
        paddingRes: Int? = null,
    ) {
        title.text = titleName

        if (drawableRes != null && positionType != null) {
            when (positionType) {
                LEFT -> title.drawableStart(drawableRes, paddingRes)
                TOP -> title.drawableTop(drawableRes, paddingRes)
                RIGHT -> title.drawableEnd(drawableRes, paddingRes)
                BOTTOM -> title.drawableBottom(drawableRes, paddingRes)
            }
        }
    }

    fun setTitleStyle(styleId: Int) {
        title.setTextAppearance(styleId)
    }

    fun setColorButtonNext(color: Int) {
        buttonNext.setTextColor(color)
    }

    fun setColorButtonBack(color: Int) {
        buttonBackText.setTextColor(color)
    }

    fun setColorBottomBorder(color: Int) {
        bottomBorder.setBackgroundColor(color)
    }

    fun setHeightBottomBorder(heightId: Int) {
        bottomBorder.layoutParams.apply {
            height = context.resources.getDimensionPixelSize(heightId)
        }
    }

    fun clickOnButtonNext(clickListener: OnClickListener?) {
        buttonNext.setOnClickListener(clickListener)
    }

    fun clickOnButtonBack(clickListener: OnClickListener?) {
        buttonBack.setOnClickListener(clickListener)
    }

    fun clickOnButtonBackText(clickListener: OnClickListener?) {
        buttonBackText.setOnClickListener(clickListener)
    }

    fun clickOnButtonMenu(clickListener: OnClickListener?) {
        buttonMenu.setOnClickListener(clickListener)
    }

    fun setStyleButtonBackText(id: Int) {
        buttonBackText.setTextAppearance(id)
    }

    fun setNextBorderButtonStyle(resId: Int) {
        buttonBorderNext.toVisible()
        buttonBorderNext.setShape(resId)
    }

    fun setVisible(visible: Boolean) {
        buttonBorderNext.visibility = if (visible) VISIBLE else GONE
    }

    fun setIconOfButtonBack(imageActionId: Int) {
        buttonBack.setImageDrawable(ContextCompat.getDrawable(context, imageActionId))
    }

    fun setIconOfButtonMenu(imageActionId: Int) {
        buttonMenu.setImageDrawable(ContextCompat.getDrawable(context, imageActionId))
    }

    fun setTitleImage(imageActionId: Int) {
        titleImage.setImageDrawable(ContextCompat.getDrawable(context, imageActionId))
    }

    fun setNextBorderButtonTextStyle(resId: Int) {
        buttonBorderNext.setStyle(resId)
    }

    fun setNextBorderButtonText(text: String) {
        buttonBorderNext.setTitle(text)
    }

    fun clickOnButtonBorderNext(clickListener: OnClickListener?) {
        buttonBorderNext.setOnClickListener(clickListener)
    }

    fun setEnabledOnButtonBorderNext(isEnabled: Boolean) {
        buttonBorderNext.isEnabled = isEnabled
    }

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    init {
        inflate(context, R.layout.main_navigation_bar, this)
        bottomBorder = findViewById(R.id.bottom_border)
        buttonMenu = findViewById(R.id.button_menu)
        buttonNext = findViewById(R.id.button_next)
        buttonBack = findViewById(R.id.button_back)
        buttonBackText = findViewById(R.id.button_back_text)
        title = findViewById(R.id.screen_title)
        titleImage = findViewById(R.id.screen_title_image)
        buttonBorderNext = findViewById(R.id.button_border_next)

        buttonBorderNext.toGone()
    }
}