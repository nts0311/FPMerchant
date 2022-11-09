package com.sonnt.fpmerchant.ui.doneorders

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.sonnt.fpmerchant.R
import com.sonnt.fpmerchant.databinding.FragmentDoneOrdersBinding
import com.sonnt.fpmerchant.databinding.ItemOrderBinding
import com.sonnt.fpmerchant.model.OrderInfo
import com.sonnt.fpmerchant.ui._base.BaseFragment
import com.sonnt.fpmerchant.ui._base.BaseRecyclerViewAdapter


class DoneOrdersFragment : BaseFragment<FragmentDoneOrdersBinding>() {
    override var layoutResId: Int = R.layout.fragment_done_orders

    private val viewModel: DoneOrdersViewModel by viewModels()
    private val adapter = BaseRecyclerViewAdapter<OrderInfo, ItemOrderBinding>(R.layout.item_order)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupBinding()
        viewModel.getDoneOrders()
    }

    private fun setupBinding() {
        viewModel.doneOrders.observe(viewLifecycleOwner) {
            adapter.items = it
        }
    }

    private fun setupView() {
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter.onItemClicked = {index, orderInfo ->

        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = DoneOrdersFragment()
    }
}