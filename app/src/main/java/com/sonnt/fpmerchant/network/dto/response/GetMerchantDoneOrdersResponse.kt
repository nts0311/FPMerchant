package com.sonnt.fpmerchant.network.dto.response

import com.sonnt.fpdriver.network.dto.response.BaseResponse
import com.sonnt.fpmerchant.model.OrderInfo

class GetMerchantDoneOrdersResponse(
    val doneOrders: List<OrderInfo>
): BaseResponse()