package com.sonnt.fpmerchant.model

import com.google.android.gms.maps.model.LatLng

data class FPMapMarker(
    var lat: Double,
    var long: Double,
    var imgId: Int? = null,
    var title: String? = null,
) {

    fun toLatLng(): LatLng = LatLng(lat, long)

}
