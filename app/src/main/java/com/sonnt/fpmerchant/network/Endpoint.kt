package com.sonnt.fpmerchant.network

class Endpoint {
    companion object {
        val BASE_URL = "https://3e03-27-72-100-124.ap.ngrok.io"
        val WS_BASE_URL = "$BASE_URL/stomp"

        val newOrderRequest = "/users/ws/merchant/newOrderRequest"
        val cancelOrder = "/merchant/order/cancel-order"
    }
}