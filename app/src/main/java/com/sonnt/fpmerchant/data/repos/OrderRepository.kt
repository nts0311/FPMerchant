package com.sonnt.fpmerchant.data.repos

import com.sonnt.fpmerchant.di.AppModule
import com.sonnt.fpmerchant.model.OrderInfo
import com.sonnt.fpmerchant.network.Endpoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach

class OrderRepository private constructor(){
    private val stompMessageHub = AppModule.provideStompMessageHub()
    val coroutineScope = CoroutineScope(Dispatchers.IO)

    var latestOrder: OrderInfo? = null

    val newOrderRequestFlow: Flow<OrderInfo>? =
        stompMessageHub.subscribeTo(Endpoint.newOrderRequest, OrderInfo::class.java)
            ?.onEach {
                latestOrder = it
            }


    companion object {
        val shared = OrderRepository()
    }
}