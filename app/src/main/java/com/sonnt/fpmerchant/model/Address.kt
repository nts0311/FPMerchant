package com.sonnt.fpmerchant.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Address(
var id: Long = 0,

var ward: String? = null,
var district: String? = null,
var city: String? = null,
var detail: String? = null,
var lat: Double? = null,
var lng: Double? = null
) : Parcelable {
    override fun toString(): String {
        var result = ""

        detail?.let { result += "$it, " }
        ward?.let { result += "$it, " }
        district?.let { result += "$it, " }
        city?.let { result += it }

        return result
    }
}