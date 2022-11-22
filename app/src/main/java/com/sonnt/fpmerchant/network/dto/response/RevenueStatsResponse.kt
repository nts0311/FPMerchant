package com.sonnt.fpmerchant.network.dto.response

import com.sonnt.fpdriver.network.dto.response.BaseResponse
import com.sonnt.fpmerchant.model.Product
import com.sonnt.fpmerchant.model.ProductCategory
import com.sonnt.fpmerchant.model.ProductMenu
import com.sonnt.fpmerchant.ui.selectdateview.MarkerData
import com.sonnt.fpmerchant.ui.stats.revenue.PieEntryStat
import com.sonnt.fpmerchant.utils.formatCurrency
import com.sonnt.fpmerchant.utils.standardFormat
import java.time.LocalDate


class RevenueStatsResponse(
    val revenueByDay: List<DayRevenueStat>,
    val revenueByCategory: List<CategoriesRevenueStat>,
    val revenueByMenu: List<MenuRevenueStat>,
    val revenueByProduct: List<ProductRevenueStat>
): BaseResponse()

data class DayRevenueStat(
    val date: String,
    val revenue: Double
): MarkerData {
    override val markerContent: String
    get() {
        val ld = LocalDate.parse(date)
        return "${ld.standardFormat()} \n ${revenue.formatCurrency()}"
    }
}

data class CategoriesRevenueStat(
    val category: ProductCategory,
    val percentage: Double
): PieEntryStat {
    override val value: Float
        get() = percentage.toFloat()
    override val label: String
        get() = category.name ?: ""
}

data class MenuRevenueStat(
    val menu: ProductMenu,
    val percentage: Double
): PieEntryStat {
    override val value: Float
        get() = percentage.toFloat()
    override val label: String
        get() = menu.name ?: ""
}

data class ProductRevenueStat(
    val productName: String,
    val productCategoryName: String,
    val productImageUrl: String?,
    val productMenuName: String,
    val productPrice: Double,
    val revenue: Double,
    val count: Int
) {
    fun getPriceStr(): String {
        return productPrice.formatCurrency()
    }

    fun getRevenueStr(): String {
        return revenue.formatCurrency()
    }
}