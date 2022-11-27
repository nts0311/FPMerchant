package com.sonnt.fpmerchant.network.service

import com.sonnt.fpmerchant.network.dto.response.OrderStatsResponse
import com.sonnt.fpmerchant.network.dto.response.RevenueStatsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface StatsService {
    @GET("merchant/stats/revenue")
    suspend fun getRevenueStat(
        @Query("fromDate") fromDate: String,
        @Query("toDate") toDate: String
    ): Response<RevenueStatsResponse>

    @GET("merchant/stats/order")
    suspend fun getOrderStat(
        @Query("fromDate") fromDate: String,
        @Query("toDate") toDate: String
    ): Response<OrderStatsResponse>
}