package com.sonnt.fpmerchant.network.service

import com.sonnt.fpmerchant.network.dto.request.AuthRequest
import com.sonnt.fpmerchant.network.dto.request.UpdateFcmTokenRequest
import com.sonnt.fpmerchant.network.dto.response.AuthenticationResponse
import com.sonnt.fpdriver.network.dto.response.BaseResponse
import com.sonnt.fpmerchant.network.dto.response.GetProductByMenuResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ProductService {
    @GET("merchant/product/get-by-menu")
    suspend fun getProductByMenu(@Query("menuId") menuI: Long): Response<GetProductByMenuResponse>
}