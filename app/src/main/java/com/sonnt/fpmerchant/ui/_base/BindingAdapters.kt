package com.sonnt.fpmerchant.ui._base

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter(
    "srcUrl",
    "placeholder",
    requireAll = false // make the attributes optional
)
fun ImageView.bindSrcUrl(
    url: String?,
    placeholder: Drawable?,
) = Glide.with(this).load(url).let { request ->
    if (placeholder != null) {
        request.placeholder(placeholder)
    }

    request.into(this)
}