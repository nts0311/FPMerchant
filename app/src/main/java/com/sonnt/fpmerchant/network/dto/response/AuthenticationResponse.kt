package com.sonnt.fpmerchant.network.dto.response

import com.sonnt.fpdriver.network.dto.response.BaseResponse

data class AuthenticationResponse(
    var userId: Long = 0L,
    var jwtToken: String = "",
    var name: String?,
    var avatarUrl: String?
) : BaseResponse()