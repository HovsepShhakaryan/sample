package com.vylo.common.widget

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import com.vylo.common.R
import com.vylo.common.ext.toVisible
import com.vylo.common.util.enums.ButtonStyle
import com.vylo.common.util.enums.ButtonType


class MainButton(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
    ConstraintLayout(context!!, attrs, defStyleAttr) {

    private val mainLayout: ConstraintLayout
    private val icon: ImageView
    private val title: TextView
    private var buttonColor: Int = R.color.primary

    fun setTitle(name: String) {
        title.text = name
    }

    fun setTitleColor(color: Int) {
        title.setTextColor(color)
    }

    fun setIcon(image: Int) {
        icon.setImageDrawable(ContextCompat.getDrawable(context, image))
        icon.toVisible()
    }

    fun setPaddingToButton(left: Int, top: Int, right: Int, bottom: Int) {
        mainLayout.setPadding(left, top, right, bottom)
    }

    fun setEnableOrDisabel(isEnable: Boolean) {
        mainLayout.isEnabled = isEnable
    }

    fun showIcon(isShow: Int) {
        icon.visibility = isShow
    }

    fun setLayoutWidth(width: Int) {
        mainLayout.layoutParams = LayoutParams(width, LayoutParams.WRAP_CONTENT)
    }

    fun setButtonColor(color: Int) {
        buttonColor = color
        val background = mainLayout.background as GradientDrawable
        background.setColor(color)
    }

    fun setShape(resId: Int) {
        mainLayout.background = ContextCompat.getDrawable(context, resId)
    }

    fun setButtonShape(type: ButtonType) {
        mainLayout.background = when (type) {
            ButtonType.BUTTON_MAIN -> ContextCompat.getDrawable(
                context,
                R.drawable.shape_main_button
            )
            ButtonType.BUTTON_ROUND -> ContextCompat.getDrawable(
                context,
                R.drawable.shape_round_button
            )
            ButtonType.BUTTON_WITHOUT_BORDER_ROUND -> ContextCompat.getDrawable(
                context,
                R.drawable.shape_without_border_round_button
            )
            ButtonType.BUTTON_ROUND_CORNER -> ContextCompat.getDrawable(
                context,
                R.drawable.shape_round_corner_button
            )
//            New style
            ButtonType.BUTTON_WHITE_ROUND -> ContextCompat.getDrawable(
                context,
                R.drawable.shape_white_round_button
            )
            ButtonType.BUTTON_GRAY_ROUND -> ContextCompat.getDrawable(
                context,
                R.drawable.shape_gray_round_button
            )
            ButtonType.BUTTON_SQUARE_DARK -> ContextCompat.getDrawable(
                context,
                R.drawable.shape_white_border
            )
            ButtonType.BUTTON_SQUARE_LIGHT -> ContextCompat.getDrawable(
                context,
                R.drawable.shape_white_without_border
            )
        }
    }

    fun setStyle(styleId: Int) {
        title.setTextAppearance(styleId)
    }

    fun setTitleMargin(leftID: Int, topId: Int, rightId: Int, bottomId: Int) {
        val left = resources.getDimensionPixelSize(leftID)
        val top = resources.getDimensionPixelSize(topId)
        val right = resources.getDimensionPixelSize(rightId)
        val bottom = resources.getDimensionPixelSize(bottomId)
        val params = title.layoutParams as LayoutParams
        params.setMargins(left, top, right, bottom)
        title.layoutParams = params
    }

    fun clickOnButton(clickListener: OnClickListener) {
        mainLayout.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    v.background.colorFilter =
                        BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                            buttonColor,
                            BlendModeCompat.SRC_ATOP
                        )
                    v.invalidate()
                }
                MotionEvent.ACTION_UP -> {
                    v.background.clearColorFilter()
                    v.invalidate()
                }
            }
            false
        }
        mainLayout.setOnClickListener(clickListener)
    }

    fun roundedWhiteButtonStyle(context: Context, title: String, buttonStyle: ButtonStyle) {
        setButtonShape(ButtonType.BUTTON_WHITE_ROUND)
        setTitle(title)
        setButtonColor(ContextCompat.getColor(context, R.color.button_white))
        when(buttonStyle) {
            ButtonStyle.ROUNDED_BIG_MEDIUM -> setStyle(R.style.White_rounded_button_big_medium)
            ButtonStyle.ROUNDED_SMALL -> setStyle(R.style.White_rounded_button_small)
        }
    }

    fun roundedGrayButtonStyle(context: Context, title: String) {
        setButtonShape(ButtonType.BUTTON_GRAY_ROUND)
        setTitle(title)
        setStyle(R.style.Gray_rounded_button_big_medium)
        setButtonColor(ContextCompat.getColor(context, R.color.button_black))
    }

    fun roundedGrayWhiteTextButtonStyle(context: Context, title: String, buttonStyle: ButtonStyle) {
        setButtonShape(ButtonType.BUTTON_GRAY_ROUND)
        setTitle(title)
        setButtonColor(ContextCompat.getColor(context, R.color.button_black))
        when(buttonStyle) {
            ButtonStyle.ROUNDED_BIG_MEDIUM -> setStyle(R.style.Gray_rounded_white_text_button_big_medium)
            ButtonStyle.ROUNDED_SMALL -> setStyle(R.style.Gray_rounded_white_text_button_small)
        }
    }

    fun squareRoundedWhiteButtonStyle(context: Context, title: String) {
        setButtonShape(ButtonType.BUTTON_SQUARE_LIGHT)
        setTitle(title)
        setStyle(R.style.White_square_rounded_button_medium_small)
        setButtonColor(ContextCompat.getColor(context, R.color.button_white))
    }

    fun squareRoundedGrayButtonStyle(context: Context, title: String) {
        setButtonShape(ButtonType.BUTTON_SQUARE_DARK)
        setTitle(title)
        setStyle(R.style.Gray_square_rounded_button_big)
        setButtonColor(ContextCompat.getColor(context, R.color.button_black))
    }

    fun squareRoundedGrayButtonGrayTextStyle(context: Context, title: String) {
        setButtonShape(ButtonType.BUTTON_SQUARE_DARK)
        setTitle(title)
        setStyle(R.style.Gray_square_rounded_button_big_gray_text)
        setButtonColor(ContextCompat.getColor(context, R.color.button_black))
    }

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    init {
        inflate(context, R.layout.main_button, this)
        mainLayout = findViewById(R.id.main_layout)
        title = findViewById(R.id.first_title)
        icon = findViewById(R.id.icon)
        icon.visibility = GONE
    }
}
