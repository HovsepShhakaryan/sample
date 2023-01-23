package com.vylo.main.settingsprivacy.presentation.fragment.security.presentation.fragment

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import com.vylo.common.BaseFragment
import com.vylo.common.entity.RespondData
import com.vylo.common.util.RESPOND_INFO
import com.vylo.common.util.RESPONSE_TYPE
import com.vylo.main.R
import com.vylo.main.databinding.FragmentSecurityBinding

class SecurityFragment : BaseFragment<FragmentSecurityBinding>() {

    override fun getViewBinding() = FragmentSecurityBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        beginning()
    }

    private fun beginning() {
        createToolbar()
        createContentOfView()
    }

    private fun createToolbar() {
        viewBinder.toolbar.apply {
            setIconOfButtonBack(com.vylo.common.R.drawable.ic_back_nav)
            clickOnButtonBack { backToPrevious() }
            setTitle(resources.getString(R.string.security))
            setStyleButtonBackText(com.vylo.common.R.style.MainText_H6_3)
            showBottomBorder(View.VISIBLE)
            setColorBottomBorder(
                ContextCompat.getColor(
                    requireContext(),
                    com.vylo.common.R.color.shadow
                )
            )
            setHeightBottomBorder(com.vylo.common.R.dimen.half_one)
        }
    }

    private fun createContentOfView() {

    }

    override fun openCreateResponseScreen(
        id: String?,
        responseType: Int,
        title: String?,
        categoryId: String?,
        categoryName: String?
    ) {
        super.openCreateResponseScreen(id, responseType, title, categoryId, categoryName)
        if (!id.isNullOrEmpty())
            navigateTo(
                R.id.navigationFragment,
                bundleOf(
                    RESPOND_INFO to RespondData(
                        responseToGlobalId = id,
                        title!!,
                        categoryId!!,
                        categoryName!!
                    ),
                    RESPONSE_TYPE to responseType
                )
            )
        else
            navigateTo(
                R.id.navigationFragment,
                bundleOf(
                    RESPONSE_TYPE to responseType
                )
            )
    }
}