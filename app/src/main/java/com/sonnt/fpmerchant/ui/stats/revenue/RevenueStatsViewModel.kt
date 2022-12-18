package com.sonnt.fpmerchant.ui.stats.revenue

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieEntry
import com.sonnt.fpmerchant.network.ApiResult
import com.sonnt.fpmerchant.network.NetworkModule
import com.sonnt.fpmerchant.network.callApi
import com.sonnt.fpmerchant.network.dto.response.DayRevenueStat
import com.sonnt.fpmerchant.network.dto.response.NumOfOrderByDayStat
import com.sonnt.fpmerchant.network.dto.response.ProductRevenueStat
import com.sonnt.fpmerchant.network.dto.response.RevenueStatsResponse
import com.sonnt.fpmerchant.ui.base.BaseViewModel
import com.sonnt.fpmerchant.utils.formatCurrency
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class LineStatEntry(
    val title: String,
    val entry: Entry
)

interface PieEntryStat {
    val value: Float
    val label: String
}

class RevenueStatsViewModel: BaseViewModel() {

    val revenueChartData = MutableLiveData<List<LineStatEntry>>()
    val revenueByCategoryChartData = MutableLiveData<List<PieEntry>>()
    val revenueByMenuChartData = MutableLiveData<List<PieEntry>>()
    val productRevenueData = MutableLiveData<List<ProductRevenueStat>>()
    val totalRevenue = MutableLiveData<String>()

    fun getRevenueStats(fromDate: LocalDate, toDate: LocalDate) {

        if(fromDate == toDate) {
            return
        }

        viewModelScope.launch {
            val response = callApi { NetworkModule.statsService.getRevenueStat(fromDate.toString(), toDate.plusDays(1).toString()) }

            when (response) {
                is ApiResult.Success -> {
                    processRevenueByDayStatsData(fromDate, toDate, response.data!!)
                    revenueByCategoryChartData.value = processRevenuePieStatsData(response.data?.revenueByCategory ?: listOf())
                    revenueByMenuChartData.value = processRevenuePieStatsData(response.data?.revenueByMenu ?: listOf())
                    productRevenueData.value = response.data?.revenueByProduct ?: listOf()
                }

                is ApiResult.Failed -> {
                    error("Lỗi lấy dữ liệu thống kê")
                }
            }
        }
    }

    private fun processRevenueByDayStatsData(fromDate: LocalDate, toDate: LocalDate, response: RevenueStatsResponse) {
        var date = fromDate

        val revenueByDays = mutableListOf<DayRevenueStat>()

        while (date != toDate) {
            val dayStat = response.revenueByDay.firstOrNull{it.date == date.toString()} ?: DayRevenueStat(date.toString(), 0.0)
            revenueByDays.add(dayStat)
            date = date.plusDays(1)
        }
        val end = response.revenueByDay.firstOrNull{it.date == toDate.toString()} ?: DayRevenueStat(date.toString(), 0.0)
        revenueByDays.add(end)

        val revenueEntries = List(revenueByDays.size) { i -> Entry(i.toFloat(), revenueByDays[i].revenue.toFloat(), revenueByDays[i]) }

        totalRevenue.value = response.revenueByDay.fold(0.0) {acc, stat -> acc + stat.revenue}.formatCurrency()

        revenueChartData.value = revenueByDays.mapIndexed { index, dayRevenueStat ->
            val currentEntryDate = LocalDate.parse(revenueByDays[index].date)
            val title = if(index % 2 == 0) currentEntryDate.format(DateTimeFormatter.ofPattern("dd/MM")) else ""
            LineStatEntry(title, revenueEntries[index])
        }
    }

    private fun processRevenuePieStatsData(data: List<PieEntryStat>): List<PieEntry> {
        val revenueByCategories = data.sortedByDescending { it.value }

        if (revenueByCategories.size > 5) {
            val pieEntries = mutableListOf<PieEntry>()

            pieEntries.addAll(
                revenueByCategories.take(4).map { PieEntry(it.value, it.label) }
            )

            val otherCategoryPercentage = revenueByCategories
                .drop(4)
                .map { it.value }
                .fold(0.0f) { acc, percentage -> acc+percentage }

            pieEntries.add(PieEntry(otherCategoryPercentage, "Khác"))

            return pieEntries
        } else {
            return revenueByCategories.map {
                PieEntry(it.value, it.label)
            }
        }
    }

}