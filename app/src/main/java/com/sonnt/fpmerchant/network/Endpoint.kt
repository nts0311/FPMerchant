package com.sonnt.fpmerchant.network

class Endpoint {
    companion object {
        val BASE_URL = "https://90b6-222-252-98-63.ap.ngrok.io"
        val WS_BASE_URL = "$BASE_URL/stomp"

        val newOrderRequest = "/users/ws/merchant/newOrderRequest"
        val orderStatus = "/users/ws/merchant/orderStatus"
        val cancelOrder = "/merchant/order/cancel-order"
    }
}