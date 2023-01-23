package com.vylo.common.widget

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import com.vylo.common.R
import com.vylo.common.ext.hideKeyboard

class MainSearchView(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
    LinearLayoutCompat(context!!, attrs, defStyleAttr) {

    private val imgSearch: ImageView
    private val imgClear: ImageView
    private val etSearch: EditText
    private val cancel: TextView
    private val cancelBack: TextView
    private val focusWrapper: View

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    fun initialize(changed: ((CharSequence?) -> Unit)? = null) {
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // no need
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (changed != null) {
                    changed(p0)
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                // no need
            }

        })

        cancel.setOnClickListener {
            clearSearchView()
        }
    }

    fun showHideCancelButton(isShow: Int) {
        cancel.visibility = isShow
    }

    fun setOnClickCancelBack(setOnClickListener: OnClickListener) {
        cancelBack.setOnClickListener(setOnClickListener)
    }

    fun showHideCancelBackButton(isShow: Int) {
        cancelBack.visibility = isShow
    }

    fun getInputText() = etSearch.text
    fun setInputText(text: String) {
        etSearch.setText(text)
    }

    fun clearSearchView() {
        etSearch.setText("")
        clearSearchFocus()
    }

    fun setHint(hintText: String) {
        etSearch.hint = hintText
    }

    private fun clearSearchFocus() {
        context.hideKeyboard(etSearch)
        focusWrapper.requestFocus()
    }

    fun showHideClearButton(isVisible: Int) {
        imgClear.visibility = isVisible
    }

    fun clearSearchViewByClearButton() {
        clearSearchView()
    }

    fun getClearButton() = imgClear

    init {
        inflate(context, R.layout.main_search_view, this)

        imgSearch = findViewById(R.id.img_search)
        imgClear = findViewById(R.id.img_clear)
        etSearch = findViewById(R.id.et_search)
        cancel = findViewById(R.id.cancel)
        cancelBack = findViewById(R.id.cancel_back)
        focusWrapper = findViewById(R.id.focus_wrapper)

//        initialize()
    }
}