package com.sonnt.fpmerchant.utils

import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import com.sonnt.fpmerchant.R

fun FragmentActivity.createTextDialog(title: String, text: String? = null,hint: String = "", positiveAction: (String) -> Unit): AlertDialog {
    val builder = AlertDialog.Builder(this)
    builder.setTitle(title)
    val menuRoot: View = layoutInflater.inflate(R.layout.dialog_text, null)
    val edt = menuRoot.findViewById<EditText>(R.id.et_add_category)
    edt.hint = hint
    if (text != null) edt.setText(text)
    builder.setView(menuRoot)
    builder.setPositiveButton("OK") { dialog, which ->
        dialog.cancel()
        positiveAction(edt.text.toString())
    }
    builder.setNegativeButton("Huỷ") { dialog, which -> dialog.cancel() }
    return builder.create()
}