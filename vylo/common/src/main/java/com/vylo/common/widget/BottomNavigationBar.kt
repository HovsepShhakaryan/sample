package com.vylo.common.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import com.vylo.common.R
import com.vylo.common.util.enums.ButtonType

class BottomNavigationBar(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
    LinearLayoutCompat(context!!, attrs, defStyleAttr) {

    private val buttonBack: ImageView
    private val kebab: ImageView
    private val responseList: ImageView
    private val buttonRespond: MainButton
    private val buttonResponses: MainButton

    fun buttonBackVisibility(isVisible: Int) {
        buttonBack.visibility = isVisible
    }

    fun kebabVisibility(isVisible: Int) {
        kebab.visibility = isVisible
    }

    fun buttonRespondVisibility(isVisible: Int) {
        buttonRespond.visibility = isVisible
    }

    fun buttonResponsesVisibility(isVisible: Int) {
        buttonResponses.visibility = isVisible
    }

    fun responseListVisibility(isVisible: Int) {
        responseList.visibility = isVisible
    }

    fun setButtonResponsesTitle(count: Long) {
        if (count > 0) {
            val title = String.format(context.getString(R.string.responses_with_counter), count)
            buttonResponses.squareRoundedGrayButtonStyle(context, title)
        } else {
            val title = context.getString(R.string.no_responses)
            buttonResponses.squareRoundedGrayButtonGrayTextStyle(context, title)
        }

    }

    fun clickOnButtonBack(clickListener: OnClickListener?) {
        buttonBack.setOnClickListener(clickListener)
    }

    fun clickOnKebab(clickListener: OnClickListener?) {
        kebab.setOnClickListener(clickListener)
    }

    fun clickOnResponseList(clickListener: OnClickListener?) {
        responseList.setOnClickListener(clickListener)
    }

    fun clickOnButtonRespond(clickListener: OnClickListener?) {
        buttonRespond.setOnClickListener(clickListener)
    }

    fun clickOnButtonResponses(clickListener: OnClickListener?) {
        buttonResponses.setOnClickListener(clickListener)
    }

    private fun initButtonBack() {
        buttonBack.setImageResource(R.drawable.ic_back_nav)
    }

    private fun initButtonKebab() {
        kebab.setImageResource(R.drawable.ic_kebab)
    }

    private fun initButtonResponseList() {
        responseList.setImageResource(R.drawable.ic_response_list)
    }

    private fun initButtonRespond() {
        buttonRespond.squareRoundedWhiteButtonStyle(context, context.resources.getString(R.string.respond))
    }

    private fun initButtonResponses() {
        setButtonResponsesTitle(0)
    }

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    init {
        inflate(context, R.layout.bottom_navigation_bar, this)

        buttonBack = findViewById(R.id.button_back)
        kebab = findViewById(R.id.kebab)
        responseList = findViewById(R.id.response_list)
        buttonRespond = findViewById(R.id.button_respond)
        buttonResponses = findViewById(R.id.button_responses)

        initButtonBack()
        initButtonKebab()
        initButtonResponseList()

        initButtonRespond()
        initButtonResponses()
    }
}