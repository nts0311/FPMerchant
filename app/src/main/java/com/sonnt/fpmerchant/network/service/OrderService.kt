package com.sonnt.fpmerchant.network.service


import com.sonnt.fpdriver.network.dto.response.BaseResponse
import com.sonnt.fpmerchant.network.dto.response.GetMerchantActiveOrdersResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface OrderService {
    @GET("merchant/order/get-active-orders")
    suspend fun getActiveOrders(): Response<GetMerchantActiveOrdersResponse>

    @GET("merchant/order/cancel-order")
    suspend fun cancelOrder(@Query("orderId") orderId: Long): Response<BaseResponse>
}