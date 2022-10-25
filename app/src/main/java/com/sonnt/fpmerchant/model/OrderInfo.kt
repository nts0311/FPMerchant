package com.sonnt.fpmerchant.model

data class OrderedProductItem(
    val name: String,
    val num: Int,
    val attributes: List<String>
) {
    fun attrsAsString(): String {
        return attributes.fold("") {init, value ->
            return@fold if(init != "") "$init\n$value" else value
        }
    }
}

class OrderEstimatedRouteInfo(
    var id: Long = 0,
    val durationInSec: Long? = null,
    val distanceInMeter: Long? = null,
    val distanceReadable: String? = null
)

data class OrderPaymentInfo(
    val price: Double,
    val deliveryFee: Double,
    val serviceFee: Double,
    val discount: Double
) {
    fun calculatePrice() : Double {
        return price + deliveryFee + serviceFee + discount
    }
}

enum class OrderStatus(val value: String) {
    INIT("INIT"),
    SEARCHING_DRIVER("SEARCHING_DRIVER"),
    PREPARING("PREPARING"),
    PICKING_UP("PICKING_UP"),
    DELIVERING("DELIVERING"),
    SUCCEED("SUCCEED"),
    CANCELED("CANCELED")
}

data class OrderInfo(
    val orderId: Long,
    val orderStatus: String,
    val createdDate: String,
    val fromAddress: Address,
    val toAddress: Address,
    val routeInfo: OrderEstimatedRouteInfo,
    val item: List<OrderedProductItem>,
    val paymentInfo: OrderPaymentInfo,
    val customerName: String,
    val customerPhone: String,
    val merchantName: String,
    val merchantPhone: String
) {
    fun getOrderStatus(): OrderStatus {
        return OrderStatus.valueOf(orderStatus)
    }
}