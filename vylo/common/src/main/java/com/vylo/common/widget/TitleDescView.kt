package com.vylo.common.widget

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.KeyEvent
import android.view.View
import android.view.View.OnKeyListener
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import com.vylo.common.R
import com.vylo.common.ext.*

class TitleDescView(context: Context, attrs: AttributeSet, defStyleAttr: Int) :
    LinearLayoutCompat(context, attrs, defStyleAttr) {

    private val title: TextView
    private val textCounter: TextView
    private val llTitle: LinearLayout
    private val desc: EditText
    private val viewDesc: ImageView

    private val descTextSize: Float
    private val descHintTextSize: Float
    private val isSetVisibleView: Boolean
    private var isVisibleDesc: Boolean = false
    private val maxTextSize = 150
    private var isTextCounterVisible = false

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)

    fun setDesc(descV: String) {
        desc.setText(descV)
    }

    fun getDesc() = desc.text.toString()

    fun onDescClickListener(click: () -> Unit) {
        desc.focusable = View.NOT_FOCUSABLE
        desc.setOnClickListener {
            click()
        }
    }

    fun disableLineBreak() {
        desc.imeOptions = EditorInfo.IME_ACTION_NONE
        desc.setOnKeyListener(OnKeyListener { v, keyCode, event -> keyCode == KeyEvent.KEYCODE_ENTER })
    }

    init {
        inflate(context, R.layout.title_desc_view, this)

        title = findViewById(R.id.title)
        textCounter = findViewById(R.id.text_counter)
        llTitle = findViewById(R.id.ll_title)
        desc = findViewById(R.id.desc)
        viewDesc = findViewById(R.id.view_desc)

        context.theme.obtainStyledAttributes(attrs, R.styleable.TitleDescView, 0, 0).apply {
            try {
                title.text = getString(R.styleable.TitleDescView_title)
                isTextCounterVisible =
                    getBoolean(R.styleable.TitleDescView_counter_visibility, false)
                if (isTextCounterVisible) {
                    textCounter.toVisible()
                }
                desc.hint = getString(R.styleable.TitleDescView_desc_hint)
                desc.setText(getString(R.styleable.TitleDescView_desc))

                isSetVisibleView = getBoolean(R.styleable.TitleDescView_visibility_label, false)
                if (isSetVisibleView) {
                    desc.transformationMethod = PasswordTransformationMethod.getInstance()

                    viewDesc.setOnClickListener {
                        if (isVisibleDesc) {
                            viewDesc.setImageResource(R.drawable.ic_eye_close)
                        } else {
                            viewDesc.setImageResource(R.drawable.ic_eye_open)
                        }
                        isVisibleDesc = !isVisibleDesc
                        desc.setPasswordVisibility(isVisibleDesc)
                    }
                }

                val defWidth = resources.getDimensionPixelSize(R.dimen.container_100)

                llTitle.layoutParams =
                    LinearLayout.LayoutParams(
                        getDimensionPixelSize(R.styleable.TitleDescView_title_width, defWidth),
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )

                descTextSize =
                    getDimension(R.styleable.TitleDescView_desc_text_size, 14f).pxToSp(context)
                descHintTextSize =
                    getDimension(R.styleable.TitleDescView_desc_hint_text_size, 14f).pxToSp(context)

                title.setTextColor(
                    getColor(
                        R.styleable.TitleDescView_title_color,
                        ContextCompat.getColor(context, R.color.white_grey_text)
                    )
                )
                desc.setTextColor(
                    getColor(
                        R.styleable.TitleDescView_desc_color,
                        ContextCompat.getColor(context, R.color.primary)
                    )
                )
                desc.setHintTextColor(
                    getColor(
                        R.styleable.TitleDescView_desc_hint_color,
                        ContextCompat.getColor(context, R.color.white_grey_text)
                    )
                )

                desc.isEnabled = getBoolean(R.styleable.TitleDescView_enable, true)

                if (desc.text.isNullOrEmpty()) {
                    desc.setTextSize(TypedValue.COMPLEX_UNIT_SP, descHintTextSize)
                } else {
                    if (isSetVisibleView) {
                        viewDesc.toVisible()
                    }
                    desc.setTextSize(TypedValue.COMPLEX_UNIT_SP, descTextSize)
                }
            } finally {
                recycle()
            }
        }

        desc.addTextChangedListener(object : TextWatcher {
            private var isHintVisible = true
            private var previousText = ""

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0?.length.orZero() <= maxTextSize) {
                    previousText = p0.toString()
                    Log.i("TitleDescView", "beforeTextChanged: ${p0.toString()}")
                }
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                textCounter.text = context.resources.getString(
                    R.string.text_counter,
                    p0?.length.orZero(),
                    maxTextSize
                )
                if (p0?.length == 0) {
                    isHintVisible = true
                    desc.setTextSize(TypedValue.COMPLEX_UNIT_SP, descHintTextSize)

                    if (isSetVisibleView) {
                        viewDesc.toGone()
                        isVisibleDesc = false
                        viewDesc.setImageResource(R.drawable.ic_eye_close)
                        desc.setPasswordVisibility(isVisibleDesc)
                    }
                } else if (isHintVisible) {
                    isHintVisible = false
                    desc.setTextSize(TypedValue.COMPLEX_UNIT_SP, descTextSize)

                    if (isSetVisibleView) {
                        viewDesc.toVisible()
                    }
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                if (isTextCounterVisible) {
                    p0?.apply {
                        if (length.orZero() > maxTextSize) {
                            desc.setText(previousText)
                            desc.setSelection(previousText.length)
                        }
                    }
                }
            }

        })
    }
}