package com.vylo.common

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.vylo.common.databinding.FragmentUpdateBinding
import com.vylo.common.util.enums.ButtonStyle

class UpdateFragment : BaseFragment<FragmentUpdateBinding>() {

    override fun getViewBinding() = FragmentUpdateBinding.inflate(layoutInflater)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinder = getViewBinding()
        return viewBinder.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        beginning()
    }

    private fun beginning() {
        viewBinder.updateButton.apply {
            roundedWhiteButtonStyle(
                context,
                resources.getString(R.string.label_update),
                ButtonStyle.ROUNDED_BIG_MEDIUM
            )
            clickOnButton {
                val packageName = context.packageName
                try {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")))
                } catch (e: ActivityNotFoundException) {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$packageName")))
                }
            }
        }

        viewBinder.buttonNotNow.setOnClickListener {
            setCancelUpdate(true)
            throwStartScreen()
            requireActivity().finish()
        }
    }

}