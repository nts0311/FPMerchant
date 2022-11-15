package com.sonnt.fpmerchant.ui.orderlist

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.sonnt.fpmerchant.R
import com.sonnt.fpmerchant.databinding.FragmentOrderListBinding
import com.sonnt.fpmerchant.databinding.ItemOrderBinding
import com.sonnt.fpmerchant.model.OrderInfo
import com.sonnt.fpmerchant.ui.base.BaseFragment
import com.sonnt.fpmerchant.ui.base.BaseRecyclerViewAdapter

class OrderListFragment : BaseFragment<FragmentOrderListBinding>() {
    override var layoutResId: Int = R.layout.fragment_order_list

    var orderList = mutableListOf<OrderInfo>()

    private val adapter = BaseRecyclerViewAdapter<OrderInfo, ItemOrderBinding>(R.layout.item_order)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //setupViews()
    }

    private fun setupViews() {
        setupRv()
    }

    private fun setupRv() {
        binding.recyclerView.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    fun setListOrders(orders: MutableList<OrderInfo>) {
        orderList = orders
        adapter.items = orderList
    }

    companion object {
        @JvmStatic
        fun newInstance() = OrderListFragment()
    }
}