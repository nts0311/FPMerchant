package com.sonnt.fpmerchant.network.service

import com.sonnt.fpmerchant.network.dto.request.AuthRequest
import com.sonnt.fpmerchant.network.dto.request.UpdateFcmTokenRequest
import com.sonnt.fpmerchant.network.dto.response.AuthenticationResponse
import com.sonnt.fpdriver.network.dto.response.BaseResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("auth/login")
    suspend fun login(@Body body: AuthRequest): Response<AuthenticationResponse>

    @POST("auth/update-fcm-token")
    suspend fun updateFcmToken(@Body body: UpdateFcmTokenRequest): Response<BaseResponse>
}