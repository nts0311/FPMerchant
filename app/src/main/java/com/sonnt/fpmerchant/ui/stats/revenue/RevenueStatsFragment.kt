package com.sonnt.fpmerchant.ui.stats.revenue

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.LargeValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.sonnt.fpmerchant.R
import com.sonnt.fpmerchant.databinding.FragmentRevenueStatsBinding
import com.sonnt.fpmerchant.databinding.ItemProductRevenueStatBinding
import com.sonnt.fpmerchant.network.dto.response.ProductRevenueStat
import com.sonnt.fpmerchant.ui.base.BaseFragment
import com.sonnt.fpmerchant.ui.base.BaseRecyclerViewAdapter
import com.sonnt.fpmerchant.ui.selectdateview.MyMarkerView
import com.sonnt.fpmerchant.ui.selectdateview.SelectDateView

class RevenueStatsFragment : BaseFragment<FragmentRevenueStatsBinding>() {
    override var layoutResId: Int = R.layout.fragment_revenue_stats

    private val viewModel: RevenueStatsViewModel by viewModels()

    private lateinit var productAdapter: BaseRecyclerViewAdapter<ProductRevenueStat, ItemProductRevenueStatBinding>

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

        setupPieChart(binding.categoryStatChart, true)
        setupPieChart(binding.menuStatChart)

        setupRecycleView()
    }

    private fun setupRecycleView() {
        productAdapter = BaseRecyclerViewAdapter(R.layout.item_product_revenue_stat)
        binding.rvProducts.adapter = productAdapter
        binding.rvProducts.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun bindViewModel() {
        viewModel.revenueChartData.observe(viewLifecycleOwner) {
            drawRevenueByDayChart(it)
        }

        viewModel.revenueByCategoryChartData.observe(viewLifecycleOwner) {
            drawPieChart(binding.categoryStatChart, it)
        }

        viewModel.revenueByMenuChartData.observe(viewLifecycleOwner) {
            drawPieChart(binding.menuStatChart, it)
        }

        viewModel.productRevenueData.observe(viewLifecycleOwner) {
            productAdapter.items = it
        }

        viewModel.totalRevenue.observe(viewLifecycleOwner) {
            binding.totalRevenueText.text = it
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
            xAxis.labelRotationAngle = 60f

            // create marker to display box when values are selected
            val mv = MyMarkerView(context, R.layout.custom_marker)
            mv.chartView = this
            marker = mv
        }
    }

    private fun setupPieChart(pieChart: PieChart, isLegendOnRight: Boolean = false ) {
        pieChart.apply {
            setTransparentCircleColor(Color.rgb(36, 36, 36))
            transparentCircleRadius = 65f
            isRotationEnabled = false
            setDrawEntryLabels(false)
            description.isEnabled = false
            isHighlightPerTapEnabled = false
            setUsePercentValues(true)
        }

        pieChart.legend.apply {
            isEnabled = true
            orientation = Legend.LegendOrientation.VERTICAL
            isWordWrapEnabled = true
            /*textSize = 12F
            formSize = 12F*/

            isWordWrapEnabled = true
            horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
            verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        }
    }

    private fun drawRevenueByDayChart(dayRevenues: List<LineStatEntry>) {
        binding.revenueByDayChart.xAxis.apply {
            valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    val xVal = value.toInt()
                    if (xVal >= dayRevenues.size || xVal < 0) return ""

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

    private fun drawPieChart(pieChart: PieChart, pieEntries: List<PieEntry>) {
        val dataSet = PieDataSet(pieEntries, "").apply {
            colors = listOf(
                Color.rgb(5, 64, 82), Color.rgb(24, 184, 130),
                Color.rgb(37, 168, 230), Color.rgb(242, 198, 7),
                Color.rgb(237, 91, 28), Color.rgb(224, 139, 90)
            )

            setDrawValues(true)
            valueTextColor = Color.rgb(255,255,255)
            valueTextSize = 14.0f
        }

        val data = PieData(dataSet)

        pieChart.data = data
        pieChart.invalidate()

    }

}