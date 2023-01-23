package com.vylo.main.responsemain.upload.presentation.fragment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.vylo.common.BaseBlurDialogFragment
import com.vylo.common.util.CATEGORY_NAME
import com.vylo.common.util.GoogleAnalytics
import com.vylo.main.component.sharedviewmodel.ActivityFragmentSharedViewModel
import com.vylo.main.databinding.FragmentUploadSuccessBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class UploadSuccessFragment : BaseBlurDialogFragment<FragmentUploadSuccessBinding>() {

    override fun getViewBinding() = FragmentUploadSuccessBinding.inflate(layoutInflater)
    private val sharedViewModel: ActivityFragmentSharedViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setWindowParams()
        beginning()
        setFragmentDelay(3000)
    }

    private fun setWindowParams() {
        dialog?.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setLayout(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
        }
    }

    private fun beginning() {
        sharedViewModel.getRespondType()?.let {
            sendAnalyticEvent(sharedViewModel.getRespondType()!!, sharedViewModel.getUploadType()!!)
        }
        viewBinder.apply {
            arguments?.getString(CATEGORY_NAME)?.let { category ->
                container.categoryTitle.text = category
            }
        }
    }

    private fun setFragmentDelay(time: Long) {
        lifecycleScope.launch {
            delay(time)
            dialog?.dismiss()
        }
    }
}