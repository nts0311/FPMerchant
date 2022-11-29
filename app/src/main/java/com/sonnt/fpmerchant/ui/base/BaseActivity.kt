package com.sonnt.fpmerchant.ui.base

import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity


open class BaseActivity: AppCompatActivity() {

}

fun Context.createDialog(
    title: String = "Thông báo",
    content: String,
    positiveButtonTitle: String = "OK",
    negativeButtonTitle: String? = null,
    positiveClicked: (() -> Unit)? = null,
    negativeClicked: (() -> Unit)? = null,
) {
    val alert = AlertDialog.Builder(this)
        .setTitle(title)
        .setMessage(content)
        .setPositiveButton(positiveButtonTitle) { _, _ ->
            positiveClicked?.let { it() }
        }
    negativeButtonTitle?.let { negativeButtonTitle ->
        alert.setNegativeButton(negativeButtonTitle) { _, _ ->
            negativeClicked?.let { it() }
        }
    }

    alert.create().show()
}