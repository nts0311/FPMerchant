package com.sonnt.fpmerchant.network.service


import com.sonnt.fpdriver.network.dto.response.BaseResponse
import com.sonnt.fpmerchant.model.ProductMenu
import com.sonnt.fpmerchant.network.dto.response.GetProductMenuListResponse
import com.sonnt.fpmerchant.network.dto.response.GetProductMenuResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ProductMenuService {
    @GET("merchant/menu/list-all")
    suspend fun getAllMenu(): Response<GetProductMenuListResponse>

    @POST("merchant/menu/add")
    suspend fun addMenu(@Body menu: ProductMenu): Response<GetProductMenuResponse>

    @POST("merchant/menu/edit")
    suspend fun editMenu(@Body menu: ProductMenu): Response<BaseResponse>
}