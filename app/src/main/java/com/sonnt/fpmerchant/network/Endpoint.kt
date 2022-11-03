package com.sonnt.fpmerchant.network

class Endpoint {
    companion object {
        val BASE_URL = "http://10.0.2.2:8081"
        val WS_BASE_URL = "$BASE_URL/stomp"

        val newOrderRequest = "/users/ws/merchant/newOrderRequest"
        val orderStatus = "/users/ws/merchant/orderStatus"
        val cancelOrder = "/merchant/order/cancel-order"
    }
}