package com.vylo.main.profilefragment.presentation.fragment

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.vylo.main.R

class DeleteDialogFragment(
    private val onCLick: () -> Unit
) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(
            requireActivity(),
            android.R.style.Theme_DeviceDefault_Dialog_NoActionBar
        )
        builder.setTitle(R.string.delete_post)
            .setMessage(R.string.action_undone)
            .setPositiveButton(R.string.label_kebab_delete_video) { _, _ ->
                onCLick()
            }
            .setNegativeButton(R.string.dialog_cancel) { _, _ ->

            }

        return builder.create()
    }
}