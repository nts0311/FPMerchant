package com.sonnt.fpmerchant.network

import com.sonnt.fpdriver.network.dto.response.BaseResponse
import com.sonnt.fpdriver.network.dto.response.asApiError
import com.sonnt.fpmerchant.di.AppModule
import com.sonnt.fpmerchant.message.SessionExpiredEvent
import org.greenrobot.eventbus.EventBus
import retrofit2.Response


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
            val gson = AppModule.provideGson()
            try {
                val errorBoy = result.errorBody() ?: return ApiResult.Failed(ApiError(message = "An error has occurred!"))
                val baseResponse = (gson.fromJson(errorBoy.charStream(), BaseResponse::class.java))

                return if (result.code() == 401) {
                    EventBus.getDefault().post(SessionExpiredEvent())
                    ApiResult.Failed(baseResponse.asApiError())
                } else {
                    ApiResult.Failed(baseResponse.asApiError())
                }

            } catch (e : Exception) {
                return ApiResult.Failed(ApiError(message = "An error has occurred!"))
            }
        }
    } catch (e: Exception) {
        val error = ApiError(message = e.message)
        return ApiResult.Failed(error)
    }
}
