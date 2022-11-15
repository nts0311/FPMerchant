package com.sonnt.fpmerchant.ui.activeorderdetail

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.sonnt.fpmerchant.R
import com.sonnt.fpmerchant.databinding.FragmentActiveOrderDetailBinding
import com.sonnt.fpmerchant.databinding.ItemOrderedItemBinding
import com.sonnt.fpmerchant.model.OrderInfo
import com.sonnt.fpmerchant.model.OrderedProductItem
import com.sonnt.fpmerchant.ui.base.BaseFragment
import com.sonnt.fpmerchant.ui.base.BaseRecyclerViewAdapter


private const val ARG_ORDER_INFO = "arg_order_info"

class ActiveOrderDetailFragment : BaseFragment<FragmentActiveOrderDetailBinding>() {

    override var layoutResId: Int = R.layout.fragment_active_order_detail
    private lateinit var orderInfo: OrderInfo
    private lateinit var adapter: BaseRecyclerViewAdapter<OrderedProductItem, ItemOrderedItemBinding>
    private val viewModel: ActiveOrderDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            orderInfo = it.getParcelable("orderInfo")!!
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        bindViewModel()
    }

    private fun initView() {
        binding.orderInfo = orderInfo
        adapter = BaseRecyclerViewAdapter(R.layout.item_ordered_item)
        adapter.items = orderInfo.item
        binding.recyclerView2.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView2.adapter = adapter

        binding.button.setOnClickListener {
            viewModel.cancelOrder(orderInfo.orderId)
        }
    }

    private fun bindViewModel() {
        viewModel.cancelOrderSuccess.observe(viewLifecycleOwner) {canceled ->
            if (canceled) {
                setFragmentResult(orderCancelStatusRequestKey, bundleOf("result" to true, "orderId" to orderInfo.orderId))
                findNavController().popBackStack()
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(orderInfo: OrderInfo) =
            ActiveOrderDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_ORDER_INFO, orderInfo)
                }
            }

        const val orderCancelStatusRequestKey = "orderCancelStatusRequestKey"
    }


}