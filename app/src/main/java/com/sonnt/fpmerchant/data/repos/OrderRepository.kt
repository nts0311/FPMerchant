package com.sonnt.fpmerchant.data.repos

import androidx.lifecycle.viewModelScope
import com.sonnt.fpmerchant.di.AppModule
import com.sonnt.fpmerchant.model.OrderInfo
import com.sonnt.fpmerchant.network.ApiResult
import com.sonnt.fpmerchant.network.Endpoint
import com.sonnt.fpmerchant.network.NetworkModule
import com.sonnt.fpmerchant.network.callApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class OrderRepository private constructor() {
    private val stompMessageHub = AppModule.provideStompMessageHub()
    private val orderService = NetworkModule.orderService


    var activeOrders = mutableListOf<OrderInfo>()
    var doneOrder = mutableListOf<OrderInfo>()

    val newOrderRequestFlow: Flow<OrderInfo>? =
        stompMessageHub.subscribeTo(Endpoint.newOrderRequest, OrderInfo::class.java)
            ?.onEach {
                activeOrders.add(it)
            }

    suspend fun getActiveOrders(): List<OrderInfo>? {
        val response = callApi { orderService.getActiveOrders() }
        return when (response) {
            is ApiResult.Success -> {
                val activeOrders = response.data?.activeOrders ?: listOf()
                this.activeOrders.clear()
                this.activeOrders.addAll(activeOrders)
                activeOrders
            }

            is ApiResult.Failed -> null
        }
    }

    suspend fun cancelOrder(orderId: Long): Boolean {
        val response = callApi { NetworkModule.orderService.cancelOrder(orderId) }

        return when (response) {
            is ApiResult.Success -> {
                activeOrders.removeAll { it.orderId == orderId }
                true
            }

            is ApiResult.Failed -> false
        }

    }

    companion object {
        val shared = OrderRepository()
    }
}