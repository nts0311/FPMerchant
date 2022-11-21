package com.sonnt.fpmerchant.ui.stats.revenue

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.LargeValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.sonnt.fpmerchant.R
import com.sonnt.fpmerchant.databinding.FragmentRevenueStatsBinding
import com.sonnt.fpmerchant.network.dto.response.DayRevenueStat
import com.sonnt.fpmerchant.ui.base.BaseFragment
import com.sonnt.fpmerchant.ui.selectdateview.MyMarkerView
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class RevenueStatsFragment : BaseFragment<FragmentRevenueStatsBinding>() {
    override var layoutResId: Int = R.layout.fragment_revenue_stats

    private val viewModel: RevenueStatsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        bindViewModel()
    }

    private fun setupViews() {
        binding.selectDateView.newDateSelected = {fromDate, toDate ->
            viewModel.getRevenueStats(fromDate, toDate)
        }

        setupRevenueDayChart(binding.revenueByDayChart)
    }

    private fun bindViewModel() {
        viewModel.revenueChartData.observe(viewLifecycleOwner) {
            drawRevenueByDayChart(it)
        }
    }

    private fun setupRevenueDayChart(chart: LineChart) {
        chart.apply {
            legend.isEnabled = false
            axisRight.isEnabled = false
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.setDrawGridLines(false)

            xAxis.setDrawAxisLine(false)
            description.isEnabled = false

            xAxis.axisMinimum = 0.0f
            //xAxis.setDrawLabels(false)

            axisLeft.valueFormatter = LargeValueFormatter().apply {
                setSuffix(arrayOf("đ", "K đ", "Tr đ", "Tỷ đ", "K.Tỷ đ"))
            }
            axisLeft.axisMinimum = 0.0f
            xAxis.labelRotationAngle = 45f

            // create marker to display box when values are selected
            val mv = MyMarkerView(context, R.layout.custom_marker)
            mv.chartView = this
            marker = mv
        }
    }

    private fun drawRevenueByDayChart(dayRevenues: List<DayRevenueEntry>) {
        binding.revenueByDayChart.xAxis.apply {
            valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    val xVal = value.toInt()
                    if (xVal >= dayRevenues.size) return ""

                    return dayRevenues[xVal].title
                }
            }
            labelCount = dayRevenues.size
        }

        val set1 = LineDataSet(dayRevenues.map { it.entry }, "")
        set1.setDrawValues(false)
        set1.setDrawCircles(false)
        set1.lineWidth = 2f
        set1.setDrawFilled(true)
        set1.color = Color.rgb(4, 194, 111)
        set1.fillColor = Color.rgb(14, 235, 139)

        val lineData = LineData(set1)
        binding.revenueByDayChart.data = lineData
        binding.revenueByDayChart.invalidate()
    }
}