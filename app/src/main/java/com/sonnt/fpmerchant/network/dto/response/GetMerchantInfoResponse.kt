package com.sonnt.fpmerchant.network.dto.response

import com.sonnt.fpdriver.network.dto.response.BaseResponse

class GetMerchantInfoResponse(
    val opening: Boolean,
    val openingHour: String,
    val closingHour: String
): BaseResponse()