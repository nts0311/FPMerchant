package com.sonnt.fpmerchant.ui.productdetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.sonnt.fpmerchant.R
import com.sonnt.fpmerchant.databinding.FragmentProductDetailBinding
import com.sonnt.fpmerchant.databinding.ItemProductAttributeBinding
import com.sonnt.fpmerchant.databinding.ItemProductAttributeOptionBinding
import com.sonnt.fpmerchant.model.Product
import com.sonnt.fpmerchant.model.ProductAttribute
import com.sonnt.fpmerchant.model.ProductAttributeOption
import com.sonnt.fpmerchant.ui._base.BaseFragment
import com.sonnt.fpmerchant.ui._base.BaseRecyclerViewAdapter
import com.sonnt.fpmerchant.ui.activeorderdetail.ActiveOrderDetailFragment
import com.sonnt.fpmerchant.ui.productattribute.ProductAttributeFragment

class ProductDetailFragment : BaseFragment<FragmentProductDetailBinding>() {
    override var layoutResId: Int = R.layout.fragment_product_detail

    private val viewModel: ProductDetailViewModel by viewModels()
    private lateinit var adapter: BaseRecyclerViewAdapter<ProductAttribute, ItemProductAttributeBinding>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            viewModel.product = it.getParcelable("product") ?: Product()
        }
        setFragmentResultListener(ProductAttributeFragment.resultKey) { requestKey, bundle ->
            val attribute: ProductAttribute = bundle.getParcelable("productAttribute") ?: return@setFragmentResultListener
            viewModel.onAddOrEditAttribute(attribute)
            bindListAttrs()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        bindViewModel()
        setupData()
    }

    private fun setupViews() {
        adapter = BaseRecyclerViewAdapter(R.layout.item_product_attribute)
        adapter.onItemLongClick = {view, i, productAttributeOption ->
            createItemPopupMenu(view, i,productAttributeOption)
        }
        adapter.onItemClicked = {i, attribute ->
            editAttribute(attribute)
        }
        binding.rvAttributes.adapter=adapter
        binding.rvAttributes.layoutManager = LinearLayoutManager(requireContext())

        binding.buttonAddAttrribute.setOnClickListener {
            toAttributeDetailScreen(null)
        }
    }

    private fun bindViewModel() {

    }

    private fun setupData() {
        binding.item = viewModel.product
        bindListAttrs()
    }

    private fun bindListAttrs() {
        adapter.items = viewModel.product.attributes
    }

    private fun createItemPopupMenu(view: View, pos: Int, attribute: ProductAttribute) {
        val popup = PopupMenu(requireContext(), view)
        val inflater: MenuInflater = popup.getMenuInflater()
        inflater.inflate(R.menu.item_popup_common_menu, popup.getMenu())
        popup.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.item_edit_product_menu -> editAttribute(attribute)
                R.id.item_delete_item -> deleteAttribute(pos, attribute)
            }

            return@setOnMenuItemClickListener true
        }
        popup.show()
    }
    private fun editAttribute(attribute: ProductAttribute) {
       toAttributeDetailScreen(attribute)
    }

    private fun deleteAttribute(pos: Int, attribute: ProductAttribute) {
        viewModel.deleteAttribute(pos)
        bindListAttrs()
    }

    private fun toAttributeDetailScreen(attribute: ProductAttribute?) {
        val bundle = bundleOf("productAttribute" to attribute )
        findNavController().navigate(R.id.productAttributeFragment, bundle)
    }

}