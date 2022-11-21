package com.sonnt.fpmerchant.network.dto.response

import com.sonnt.fpdriver.network.dto.response.BaseResponse

class ProductStatsResponse(
    val productStats: List<ProductCountStat>
): BaseResponse()

data class ProductCountStat(
    val productName: String,
    val imageUrl: String?,
    val count: Int
)
