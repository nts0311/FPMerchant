package com.sonnt.fpmerchant.network.service

import com.sonnt.fpdriver.network.dto.response.BaseResponse
import com.sonnt.fpmerchant.network.dto.request.ChangeMerchantActiveHourRequest
import com.sonnt.fpmerchant.network.dto.request.ChangeMerchantActivityStatusRequest
import com.sonnt.fpmerchant.network.dto.response.GetMerchantInfoResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface InfoService {
    @GET("merchant/info/get")
    suspend fun getMerchantInfo(): Response<GetMerchantInfoResponse>

    @POST("merchant/info/change-activity-status")
    suspend fun changeActivityStatus(@Body body: ChangeMerchantActivityStatusRequest): Response<BaseResponse>

    @POST("merchant/info/change-active-hour")
    suspend fun changeActiveHour(@Body body: ChangeMerchantActiveHourRequest): Response<BaseResponse>
}