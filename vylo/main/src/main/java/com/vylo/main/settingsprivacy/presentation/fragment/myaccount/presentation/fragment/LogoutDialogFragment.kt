package com.vylo.main.settingsprivacy.presentation.fragment.myaccount.presentation.fragment

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.vylo.main.R

class LogoutDialogFragment(
    private val title: String,
    private val click: () -> Unit
) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(
            requireActivity(),
            android.R.style.Theme_DeviceDefault_Dialog_NoActionBar
        )
            .setTitle(title)
            .setMessage(requireContext().resources.getString(R.string.logout_dialog_message))
            .setPositiveButton(requireContext().resources.getString(R.string.log_out)) { _, _ ->
                click()
            }
            .setNegativeButton(requireContext().resources.getString(R.string.dialog_cancel)) { _, _ ->

            }

        return builder.create()
    }
}