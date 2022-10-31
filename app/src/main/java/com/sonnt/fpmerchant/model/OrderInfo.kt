package com.sonnt.fpmerchant.model

import android.os.Parcelable
import com.sonnt.fpmerchant.utils.formatCurrency
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Parcelize
data class OrderedProductItem(
    val name: String,
    val num: Int,
    val attributes: List<String>
) : Parcelable {
    fun attrsAsString(): String {
        return attributes.fold("") {init, value ->
            return@fold if(init != "") "$init\n$value" else value
        }
    }
}

@Parcelize
class OrderEstimatedRouteInfo(
    var id: Long = 0,
    val durationInSec: Long? = null,
    val distanceInMeter: Long? = null,
    val distanceReadable: String? = null
) : Parcelable

@Parcelize
data class OrderPaymentInfo(
    val price: Double,
    val deliveryFee: Double,
    val serviceFee: Double,
    val discount: Double
) : Parcelable {
    fun calculatePrice() : Double {
        return price + deliveryFee + serviceFee + discount
    }
}

@Parcelize
data class DriverDTO(
    val name: String,
    val phone: String,
    val driverPlate: String
) : Parcelable

@Parcelize
enum class OrderStatus(val value: String) : Parcelable {
    INIT("INIT"),
    SEARCHING_DRIVER("SEARCHING_DRIVER"),
    PREPARING("PREPARING"),
    PICKING_UP("PICKING_UP"),
    DELIVERING("DELIVERING"),
    SUCCEED("SUCCEED"),
    CANCELED("CANCELED")
}

@Parcelize
data class OrderInfo(
    val orderId: Long,
    val orderStatus: String,
    val createdDate: String,
    val driverAcceptTime: String,
    val fromAddress: Address,
    val toAddress: Address,
    val routeInfo: OrderEstimatedRouteInfo,
    val item: List<OrderedProductItem>,
    val paymentInfo: OrderPaymentInfo,
    val customerName: String,
    val customerPhone: String,
    val merchantName: String,
    val merchantPhone: String,
    val driverInfo: DriverDTO?
) : Parcelable {
    fun getOrderStatus(): OrderStatus {
        return OrderStatus.valueOf(orderStatus)
    }

    fun getProductPrice(): String {
        return paymentInfo.price.formatCurrency()
    }

    fun getHourCreated(): String {
        val ldt = LocalDateTime.parse(createdDate)
        return ldt.format(DateTimeFormatter.ofPattern("HH:mm"))
    }

    fun getDriverAcceptTimeStr(): String {
        val ldt = LocalDateTime.parse(driverAcceptTime)
        return ldt.format(DateTimeFormatter.ofPattern("dd/MM, HH:mm"))
    }

    fun getEstimatedDriverArrivalTime(): String {
        val ldt = LocalDateTime.parse(driverAcceptTime)
        val estimatedArrivalTime = ldt.plusMinutes(20)
        return estimatedArrivalTime.format(DateTimeFormatter.ofPattern("HH:mm"))
    }
}