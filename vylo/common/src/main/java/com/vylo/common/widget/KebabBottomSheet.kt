package com.vylo.common.widget

import android.graphics.drawable.ClipDrawable.HORIZONTAL
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.vylo.common.R
import com.vylo.common.adapter.HamburgerOptionAdapter
import com.vylo.common.adapter.KebabOptionAdapter
import com.vylo.common.adapter.KebabProfileOptionAdapter
import com.vylo.common.adapter.decorator.MarginItemDecoration
import com.vylo.common.adapter.entity.KebabItem
import com.vylo.common.ext.toGone
import com.vylo.common.util.enums.KebabType

class KebabBottomSheet(
    private val kebabType: KebabType = KebabType.COMMON
) : BottomSheetDialogFragment() {

    private lateinit var title: TextView
    private lateinit var list: RecyclerView
//    private lateinit var close: TextView

    private lateinit var kebabItem: KebabItem

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        val view = when (kebabType) {
            KebabType.COMMON -> inflater.inflate(R.layout.kebab_bottom_sheet, container, false)
            KebabType.REPORT -> inflater.inflate(R.layout.report_bottom_sheet, container, false)
            KebabType.PROFILE -> inflater.inflate(
                R.layout.profile_kebab_bottom_sheet,
                container,
                false
            )
            KebabType.HAMBURGER -> inflater.inflate(
                R.layout.hamburger_bottom_sheet,
                container,
                false
            )
        }

        list = view.findViewById(R.id.option_list)
        list.layoutManager = LinearLayoutManager(context)
        when (kebabType) {
            KebabType.COMMON,
            KebabType.REPORT -> {
                initKebab(view)
                list.adapter = KebabOptionAdapter(kebabItem.options)
            }
            KebabType.PROFILE -> {
                initKebab(view)
                list.adapter = KebabProfileOptionAdapter(kebabItem.options)
                list.addItemDecoration(
                    MarginItemDecoration(
                        top = resources.getDimensionPixelSize(
                            R.dimen.margin_padding_size_medium_mid_mid
                        ),
                        bottom = resources.getDimensionPixelSize(
                            R.dimen.margin_padding_size_small_large
                        ),
                        isTopMargin = true
                    )
                )
                ContextCompat.getDrawable(requireContext(), R.drawable.shape_profile_kebab_divider)
                    ?.let {
                        val itemDecorator = DividerItemDecoration(requireContext(), HORIZONTAL)
                        itemDecorator.setDrawable(it)
                        list.addItemDecoration(itemDecorator)
                    }
            }
            KebabType.HAMBURGER -> {
                list.adapter = HamburgerOptionAdapter(kebabItem.options)
            }
        }

        return view
    }

    override fun onStart() {
        super.onStart()
        val behavior = BottomSheetBehavior.from(requireView().parent as View)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun initKebab(view: View) {
        title = view.findViewById(R.id.first_title)
//        close = view.findViewById(R.id.close)

        if (kebabItem.title != null) {
            title.text = kebabItem.title
        } else {
            title.toGone()
        }

        kebabItem.titleColorRes?.let {
            title.setTextColor(it)
        }

//        close.text = resources.getString(R.string.label_close)
//        close.setOnClickListener { dismiss() }
    }

    fun setKebabItem(item: KebabItem) {
        kebabItem = item
    }

    override fun getTheme(): Int {
        return when (kebabType) {
            KebabType.HAMBURGER -> R.style.KebabHamburgerBottomSheetDialog
            else -> R.style.KebabBottomSheetDialog
        }
    }
}