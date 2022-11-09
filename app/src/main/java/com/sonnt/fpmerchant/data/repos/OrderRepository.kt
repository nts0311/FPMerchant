package com.sonnt.fpmerchant.data.repos

import androidx.lifecycle.viewModelScope
import com.sonnt.fpmerchant.di.AppModule
import com.sonnt.fpmerchant.model.OrderInfo
import com.sonnt.fpmerchant.network.ApiResult
import com.sonnt.fpmerchant.network.Endpoint
import com.sonnt.fpmerchant.network.NetworkModule
import com.sonnt.fpmerchant.network.callApi
import com.sonnt.fpmerchant.network.stomp.WSMessage
import com.sonnt.fpmerchant.network.stomp.WSMessageCode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

class OrderRepository private constructor() {
    private val stompMessageHub = AppModule.provideStompMessageHub()
    private val orderService = NetworkModule.orderService
    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    var activeOrders: MutableList<OrderInfo> = Collections.synchronizedList(mutableListOf<OrderInfo>())
    var doneOrder: MutableList<OrderInfo> = Collections.synchronizedList(mutableListOf<OrderInfo>())

    private var newOrderRequestFlow: Flow<OrderInfo>? = null
    private var orderCompletedFlow: Flow<OrderInfo>? = null

    fun getNewOrderRequestFlow(): Flow<OrderInfo> {
        if (newOrderRequestFlow == null) {
            newOrderRequestFlow = stompMessageHub.subscribeTo(Endpoint.newOrderRequest, OrderInfo::class.java)
                ?.onEach {
                    activeOrders.add(it)
                }
        }

        return newOrderRequestFlow!!
    }

    fun getOrderCompletedFlow(): Flow<OrderInfo> {
        if (orderCompletedFlow == null) {
            orderCompletedFlow = stompMessageHub.subscribeTo(Endpoint.orderStatus, WSMessage::class.java)
                ?.filter { it.code == WSMessageCode.ORDER_COMPLETED.code }
                ?.map {mess ->
                    val orderId = mess.body.toLong()
                    return@map activeOrders.first { it.orderId == orderId }
                }
                ?.onEach {order ->
                    doneOrder.add(order)
                    activeOrders.removeAll { it.orderId == order.orderId }
                }
                ?.shareIn(coroutineScope, SharingStarted.Eagerly, 0)
        }

        return orderCompletedFlow!!
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

    suspend fun getDoneOrders(): List<OrderInfo>? {
        val response = callApi { orderService.getDoneOrders() }
        return when (response) {
            is ApiResult.Success -> {
                val doneOrder = response.data?.doneOrders ?: listOf()
                this.doneOrder.clear()
                this.doneOrder.addAll(activeOrders)
                doneOrder
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