package com.sonnt.fpmerchant.network.dto.response

import com.google.gson.annotations.SerializedName
import com.sonnt.fpdriver.network.dto.response.BaseResponse
import com.sonnt.fpmerchant.ui.selectdateview.MarkerData

data class OrderStatsResponse(
    val orderNum: Int,
    val numOfOrderByDay: List<NumOfOrderByDayStat>,
    val averageNumOfOrderByHour: List<AverageNumOfOrderByHourStat>
): BaseResponse()

data class NumOfOrderByDayStat(
    val date: String,
    val numOfOrder: Int
): MarkerData {
    override val markerContent: String
        get() = "$date\n$numOfOrder"
}

data class AverageNumOfOrderByHourStat(
    @SerializedName("date")
    val time: String,
    val averageNumOfOrders: Double
): MarkerData {
    override val markerContent: String
        get() = "$time\n$averageNumOfOrders"
}