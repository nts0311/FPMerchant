package com.sonnt.fpdriver.network.dto.response

import com.sonnt.fpmerchant.network.ApiError

open class BaseResponse(
    val httpStatus: Int? = null,
    val code: String? = null,
    val msg: String? = null
) {
    val isSuccess: Boolean
        get() = code == "0"
}

fun BaseResponse.asApiError(): ApiError {
    return ApiError(httpStatus, code, msg)
}