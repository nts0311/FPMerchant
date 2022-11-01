package com.sonnt.fpmerchant.network

class Endpoint {
    companion object {
        val BASE_URL = "https://fadf-2402-800-6173-bfd-1418-4992-a786-904c.ap.ngrok.io"
        val WS_BASE_URL = "$BASE_URL/stomp"

        val newOrderRequest = "/users/ws/merchant/newOrderRequest"
        val cancelOrder = "/merchant/order/cancel-order"
    }
}