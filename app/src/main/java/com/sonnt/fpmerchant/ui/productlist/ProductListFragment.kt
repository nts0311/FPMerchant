package com.sonnt.fpmerchant.ui.productlist

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.PopupMenu
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.sonnt.fpmerchant.R
import com.sonnt.fpmerchant.databinding.FragmentProductListBinding
import com.sonnt.fpmerchant.databinding.ItemProductBinding
import com.sonnt.fpmerchant.model.Product
import com.sonnt.fpmerchant.ui._base.BaseFragment
import com.sonnt.fpmerchant.ui._base.BaseRecyclerViewAdapter

class ProductListFragment : BaseFragment<FragmentProductListBinding>() {

    override var layoutResId: Int = R.layout.fragment_product_list

    private val viewModel: ProductListViewModel by viewModels()
    private val adapter =
        BaseRecyclerViewAdapter<Product, ItemProductBinding>(R.layout.item_product)

    private var menuId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        arguments?.let {
            menuId = it.getLong("menuId")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        setupBinding()
        viewModel.getProductByMenu(menuId)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.fragment_product_menu_list, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        requireActivity().menuInflater.inflate(R.menu.fragment_product_option_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.item_add_product_menu) {
            addMenu()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    private fun setupBinding() {
        viewModel.listProduct.observe(viewLifecycleOwner) {
            adapter.items = it
        }
        viewModel.errorMessage.observe(viewLifecycleOwner) {
            toast(it)
        }
    }

    private fun setupView() {
        registerForContextMenu(binding.recyclerView)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter.onItemClicked = { index, product ->
            toProductDetailScreen(product)
        }

        adapter.onItemLongClick = { view, pos, item ->
            createItemPopupMenu(view, item)
        }
    }

    private fun addMenu() {

    }

    private fun createItemPopupMenu(view: View, product: Product) {
        val popup = PopupMenu(requireContext(), view)
        val inflater: MenuInflater = popup.getMenuInflater()
        inflater.inflate(R.menu.item_popup_common_menu, popup.getMenu())
        popup.setOnMenuItemClickListener {
            if (it.itemId == R.id.item_edit_product_menu) {
                toProductDetailScreen(product)
            }

            return@setOnMenuItemClickListener true
        }
        popup.show()
    }

    fun toProductDetailScreen(product: Product) {
        val bundle = bundleOf("product" to product, "menuId" to menuId )
        findNavController().navigate(R.id.productDetailFragment, bundle)
    }

    companion object {
        @JvmStatic
        fun newInstance(menuId: Long) = ProductListFragment()
    }
}
