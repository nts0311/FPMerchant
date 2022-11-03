/*
package com.sonnt.fpmerchant.ui.productlist

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.sonnt.fpmerchant.R
import com.sonnt.fpmerchant.databinding.FragmentProductListBinding
import com.sonnt.fpmerchant.databinding.FragmentProductMenuListBinding
import com.sonnt.fpmerchant.databinding.ItemProductBinding
import com.sonnt.fpmerchant.databinding.ItemProductMenuBinding
import com.sonnt.fpmerchant.model.Product
import com.sonnt.fpmerchant.model.ProductMenu
import com.sonnt.fpmerchant.ui._base.BaseFragment
import com.sonnt.fpmerchant.ui._base.BaseRecyclerViewAdapter
import com.sonnt.fpmerchant.ui.menu.ProductMenuListFragment

class ProductListFragment : BaseFragment<FragmentProductListBinding>() {

    override var layoutResId: Int = R.layout.fragment_product_menu_list

    private val viewModel: ProductListViewModel by viewModels()
    private val adapter =
        BaseRecyclerViewAdapter<Product, ItemProductBinding>(R.layout.item_product)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        setupBinding()
        viewModel.getMenuList()
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
        requireActivity().menuInflater.inflate(R.menu.main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.item_add_product_menu) {
            addMenu()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    private fun setupBinding() {
        viewModel.menus.observe(viewLifecycleOwner) {
            adapter.items = it
        }
    }

    private fun setupView() {
        registerForContextMenu(binding.recyclerView)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter.onItemClicked = { index, orderInfo ->

        }

        adapter.onItemLongClick = { view, pos, item ->
            createItemPopupMenu(view, item)
        }
    }

    private fun addMenu() {

    }

    private fun createItemPopupMenu(view: View, productMenu: ProductMenu) {
        val popup = PopupMenu(requireContext(), view)
        val inflater: MenuInflater = popup.getMenuInflater()
        inflater.inflate(R.menu.product_menu_item_popup_menu, popup.getMenu())
        popup.setOnMenuItemClickListener {
            if (it.itemId == R.id.item_edit_product_menu) {
                editMenu(productMenu)
            }

            return@setOnMenuItemClickListener true
        }
        popup.show()
    }

    private fun editMenu(productMenu: ProductMenu) {

    }

    companion object {
        @JvmStatic
        fun newInstance() = ProductListFragment()
    }
}*/
