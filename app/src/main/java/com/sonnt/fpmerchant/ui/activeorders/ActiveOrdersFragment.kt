package com.sonnt.fpmerchant.ui.activeorders

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.sonnt.fpmerchant.R
import com.sonnt.fpmerchant.databinding.FragmentActiveOrdersBinding
import com.sonnt.fpmerchant.databinding.ItemOrderBinding
import com.sonnt.fpmerchant.model.OrderInfo
import com.sonnt.fpmerchant.ui.base.BaseFragment
import com.sonnt.fpmerchant.ui.base.BaseRecyclerViewAdapter
import com.sonnt.fpmerchant.ui.activeorderdetail.ActiveOrderDetailFragment

class ActiveOrdersFragment : BaseFragment<FragmentActiveOrdersBinding>() {

    override var layoutResId: Int = R.layout.fragment_active_orders

    private val viewModel: ActiveOrdersViewModel by viewModels()
    private val adapter = BaseRecyclerViewAdapter<OrderInfo, ItemOrderBinding>(R.layout.item_order)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFragmentResultListener(ActiveOrderDetailFragment.orderCancelStatusRequestKey) { requestKey, bundle ->
            val hasCanceledOrder = bundle.getBoolean("result")
            if (hasCanceledOrder) {
                adapter.notifyDataSetChanged()
            }
        }
        setupView()
        setupBinding()
        viewModel.getActiveOrders()
    }

    private fun setupBinding() {
        viewModel.activeOrders.observe(viewLifecycleOwner) {
            adapter.items = it
        }
    }

    private fun setupView() {
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter.onItemClicked = {index, orderInfo ->
            val bundle = bundleOf("orderInfo" to orderInfo)
            findNavController().navigate(R.id.activeOrderDetailFragment, bundle)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = ActiveOrdersFragment()
    }
}