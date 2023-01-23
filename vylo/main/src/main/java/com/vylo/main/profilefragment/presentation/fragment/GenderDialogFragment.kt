package com.vylo.main.profilefragment.presentation.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.vylo.main.R

class GenderDialogFragment(
    private val onCLick: (String) -> Unit
) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = LayoutInflater.from(requireActivity())
            .inflate(R.layout.dialog_gender, null)

        val maleItem = view.findViewById<TextView>(R.id.male_item)
        val femaleItem = view.findViewById<TextView>(R.id.female_item)
        val otherItem = view.findViewById<TextView>(R.id.other_item)

        maleItem.setOnClickListener { onItemClick(it) }
        femaleItem.setOnClickListener { onItemClick(it) }
        otherItem.setOnClickListener { onItemClick(it) }

        val builder = AlertDialog.Builder(
            requireActivity(),
            android.R.style.Theme_DeviceDefault_Dialog_NoActionBar
        )
            .setView(view)
            .setTitle(R.string.label_profile_gender_hint)

        return builder.show()
    }

    private fun onItemClick(view: View) {
        onCLick((view as TextView).text.toString())
        dismiss()
    }
}