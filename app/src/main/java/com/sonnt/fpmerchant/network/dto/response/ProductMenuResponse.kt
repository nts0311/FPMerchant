package com.sonnt.fpmerchant.network.dto.response

import com.sonnt.fpdriver.network.dto.response.BaseResponse
import com.sonnt.fpmerchant.model.ProductMenu

class GetProductMenuListResponse(
    val menus: List<ProductMenu>
): BaseResponse() {
}

class GetProductMenuResponse(
    val menu: ProductMenu
): BaseResponse()

