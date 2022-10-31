package com.sonnt.fpmerchant.network

import com.sonnt.fpdriver.network.dto.response.BaseResponse
import com.sonnt.fpdriver.network.dto.response.asApiError
import com.sonnt.fpmerchant.FpMerchantApplication
import com.sonnt.fpmerchant.utils.EventCode
import com.sonnt.fpmerchant.utils.EventHub
import retrofit2.Response
import java.lang.Exception

data class ApiError(
    var httpStatus: Int? = null,
    var code: String? = null,
    var message: String? = null
)

sealed class ApiResult<T> {
    class Success<T>(var data: T?): ApiResult<T>()
    class Failed<T>(val error: ApiError?): ApiResult<T>()
}

suspend fun <T> callApi(call: suspend () -> Response<T>): ApiResult<T?> where T: BaseResponse {
    try {
        val result = call()

        val responseBody = result.body()

        if (result.isSuccessful) {
            val baseResponse = (responseBody as? BaseResponse?) ?: return ApiResult.Failed(
                ApiError(
                    message = "An error has occurred!"
                )
            )
            return if (baseResponse.isSuccess){
                ApiResult.Success(responseBody)
            } else {
                ApiResult.Failed(baseResponse.asApiError())
            }
        } else {
            val baseResponse = (responseBody as? BaseResponse?) ?: return ApiResult.Failed(
                ApiError(
                    message = "An error has occurred!"
                )
            )
            return if (result.code() == 401) {
                //FpMerchantApplication.instance.showLoginScreen()
                EventHub.post(EventCode.sessionExpired.rawValue)
                ApiResult.Failed(baseResponse.asApiError())
            } else {
                ApiResult.Failed(baseResponse.asApiError())
            }
        }
    } catch (e: Exception) {
        val error = ApiError(message = e.message)
        return ApiResult.Failed(error)
    }
}
