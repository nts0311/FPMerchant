package com.sonnt.fpmerchant.network.dto.response

import com.sonnt.fpdriver.network.dto.response.BaseResponse
import com.sonnt.fpmerchant.model.Product

class ProductResponse(
    val product: Product
): BaseResponse() {
}