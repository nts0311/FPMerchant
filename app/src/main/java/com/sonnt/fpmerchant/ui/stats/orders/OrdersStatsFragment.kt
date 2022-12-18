package com.sonnt.fpmerchant.ui.stats.orders

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.viewModels
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.ValueFormatter
import com.sonnt.fpmerchant.R
import com.sonnt.fpmerchant.databinding.FragmentOrdersStatsBinding
import com.sonnt.fpmerchant.ui.base.BaseFragment
import com.sonnt.fpmerchant.ui.selectdateview.MyMarkerView
import com.sonnt.fpmerchant.ui.selectdateview.SelectDateView
import com.sonnt.fpmerchant.ui.stats.revenue.LineStatEntry

class OrdersStatsFragment : BaseFragment<FragmentOrdersStatsBinding>() {
    override var layoutResId: Int = R.layout.fragment_orders_stats

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

        setupChart(binding.numOfOrderByDayChart)
        setupChart(binding.averageNumOfOrderByHourChart)
    }

    private fun bindViewModel() {
       viewModel.totalOrder.observe(viewLifecycleOwner) {
           binding.orderNumText.text = it.toString()
       }

        viewModel.orderByDayChartData.observe(viewLifecycleOwner) {
            drawRevenueByDayChart(binding.numOfOrderByDayChart, it)
        }

        viewModel.averageNumOfOrderByHourChartData.observe(viewLifecycleOwner) {
            drawRevenueByDayChart(binding.averageNumOfOrderByHourChart, it)
        }
    }

    private fun setupChart(chart: LineChart) {
        chart.apply {
            legend.isEnabled = false
            axisRight.isEnabled = false
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.setDrawGridLines(false)

            xAxis.setDrawAxisLine(false)
            description.isEnabled = false

            xAxis.axisMinimum = 0.0f
            //xAxis.setDrawLabels(false)

            axisLeft.axisMinimum = 0.0f
            xAxis.labelRotationAngle = 60f

            // create marker to display box when values are selected
            val mv = MyMarkerView(context, R.layout.custom_marker)
            mv.chartView = this
            marker = mv
        }
    }

    private fun drawRevenueByDayChart(chart: LineChart, data: List<LineStatEntry>) {
        chart.xAxis.apply {
            valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    val xVal = value.toInt()
                    if (xVal >= data.size || xVal < 0) return ""

                    return data[xVal].title
                }
            }
            labelCount = data.size
        }

        val set1 = LineDataSet(data.map { it.entry }, "")
        set1.setDrawValues(false)
        set1.setDrawCircles(false)
        set1.lineWidth = 2f
        set1.setDrawFilled(true)

        if (chart == binding.numOfOrderByDayChart) {
            set1.color = Color.rgb(3, 155, 229)
            set1.fillColor = Color.rgb(41, 182, 252)
        } else {
            set1.color = Color.rgb(211, 47, 47)
            set1.fillColor = Color.rgb(244, 67, 54)
        }

        val lineData = LineData(set1)
        chart.data = lineData
        chart.invalidate()
    }

}