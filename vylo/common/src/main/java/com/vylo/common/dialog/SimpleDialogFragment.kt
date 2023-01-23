package com.vylo.common.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class SimpleDialogFragment(
    private val title: String,
    private val message: String,
    private val okButtonTitle: String,
    private val cancelButtonTitle: String? = null,
    private val onOkClick: () -> Unit,
    private val onCancelClick: (() -> Unit)? = null
) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(
            requireActivity(),
            android.R.style.Theme_DeviceDefault_Dialog_NoActionBar
        )
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(okButtonTitle) { _, _ ->
                onOkClick()
            }
            .setNegativeButton(cancelButtonTitle) { _, _ ->
                onCancelClick?.let {
                    it()
                }
            }

        return builder.create()
    }
}