package com.sonnt.fpmerchant.ui.stats.orders

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.mikephil.charting.data.Entry
import com.sonnt.fpmerchant.network.ApiResult
import com.sonnt.fpmerchant.network.NetworkModule
import com.sonnt.fpmerchant.network.callApi
import com.sonnt.fpmerchant.network.dto.response.*
import com.sonnt.fpmerchant.ui.base.BaseViewModel
import com.sonnt.fpmerchant.ui.stats.revenue.LineStatEntry
import com.sonnt.fpmerchant.utils.formatCurrency
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class RevenueStatsViewModel: BaseViewModel() {

    val orderByDayChartData = MutableLiveData<List<LineStatEntry>>()
    val averageNumOfOrderByHourChartData = MutableLiveData<List<LineStatEntry>>()
    val totalOrder = MutableLiveData<Int>()

    fun getOrderStats(fromDate: LocalDate, toDate: LocalDate) {

        if(fromDate == toDate) {
            return
        }

        viewModelScope.launch {
            val response = callApi { NetworkModule.statsService.getOrderStat(fromDate.toString(), toDate.plusDays(1).toString()) }

            when (response) {
                is ApiResult.Success -> {
                    val data = response.data!!
                    totalOrder.value = data.orderNum
                    processNumOfOrderByDayStatsData(fromDate, toDate, data)
                    processAverageNumOfOrderByDayStatsData(data)
                }

                is ApiResult.Failed -> {
                    error("Lỗi lấy dữ liệu thống kê")
                }
            }
        }
    }

    private fun processNumOfOrderByDayStatsData(fromDate: LocalDate, toDate: LocalDate, response: OrderStatsResponse) {
        var date = fromDate

        val numOfOrderByDays = mutableListOf<NumOfOrderByDayStat>()

        while (date != toDate) {
            val dayStat = response.numOfOrderByDay.firstOrNull{it.date == date.toString()} ?: NumOfOrderByDayStat(date.toString(), 0)
            numOfOrderByDays.add(dayStat)
            date = date.plusDays(1)
        }

        val end = response.numOfOrderByDay.firstOrNull{it.date == toDate.toString()} ?: NumOfOrderByDayStat(date.toString(), 0)
        numOfOrderByDays.add(end)

        val revenueEntries = List(numOfOrderByDays.size) { i -> Entry(i.toFloat(), numOfOrderByDays[i].numOfOrder.toFloat()) }

        orderByDayChartData.value = numOfOrderByDays.mapIndexed { index, statEntry ->
            val currentEntryDate = LocalDate.parse(numOfOrderByDays[index].date)
            val title = if(index % 2 == 0) currentEntryDate.format(DateTimeFormatter.ofPattern("dd/MM")) else ""
            revenueEntries[index].data = statEntry

            LineStatEntry(title, revenueEntries[index])
        }
    }

    private fun processAverageNumOfOrderByDayStatsData(response: OrderStatsResponse) {
        var time = LocalTime.of(0, 0)
        var result =  mutableListOf<LineStatEntry>()

        for (i in 0 until 24) {
            val timeFrame = "${time.standardFormat()}-${time.plusHours(1).standardFormat()}"
            val title = "${time.chartTitle()}-${time.plusHours(1).chartTitle()}"
            val entry = response.averageNumOfOrderByHour
                .firstOrNull {it.time == timeFrame }?.let {
                val entry = Entry(i.toFloat(), it.averageNumOfOrders.toFloat())
                entry.data = it
                    LineStatEntry(title, entry)
            } ?: LineStatEntry(title, Entry(i.toFloat(), 0f, timeFrame))

            result.add(entry)

            time = time.plusHours(1)
        }

        averageNumOfOrderByHourChartData.value = result
    }

}

fun LocalTime.standardFormat(): String {
    return this.format(DateTimeFormatter.ofPattern("HH:mm"))
}

fun LocalTime.chartTitle(): String {
    return "${this.hour}h"
}