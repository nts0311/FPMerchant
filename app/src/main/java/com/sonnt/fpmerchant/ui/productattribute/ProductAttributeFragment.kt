package com.sonnt.fpmerchant.ui.productattribute

import android.os.Bundle
import android.view.MenuInflater
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.sonnt.fpmerchant.R
import com.sonnt.fpmerchant.databinding.FragmentProductAttributeBinding
import com.sonnt.fpmerchant.databinding.ItemProductAttributeOptionBinding
import com.sonnt.fpmerchant.model.ProductAttribute
import com.sonnt.fpmerchant.model.ProductAttributeOption
import com.sonnt.fpmerchant.ui.base.BaseFragment
import com.sonnt.fpmerchant.ui.base.BaseRecyclerViewAdapter


class ProductAttributeFragment : BaseFragment<FragmentProductAttributeBinding>() {
    override var layoutResId: Int = R.layout.fragment_product_attribute

    private val viewModel: ProductAttributeViewModel by viewModels()
    private lateinit var adapter: BaseRecyclerViewAdapter<ProductAttributeOption, ItemProductAttributeOptionBinding>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val productAttribute: ProductAttribute? = it.getParcelable("productAttribute")
            if (productAttribute != null) {
                viewModel.productAttribute = productAttribute
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setupData()
    }

    private fun setupViews() {
        adapter = BaseRecyclerViewAdapter(R.layout.item_product_attribute_option)
        adapter.onItemLongClick = {view, i, productAttributeOption ->
            createItemPopupMenu(view, i,productAttributeOption)
        }
        adapter.onItemClicked = {i, productAttributeOption ->
            editOption(productAttributeOption)
        }
        binding.rvSelections.adapter=adapter
        binding.rvSelections.layoutManager = LinearLayoutManager(requireContext())

        binding.buttonAddSelection.setOnClickListener {
            createOptionDialog(null) {
                onAddOption(it)
            }
        }

        binding.btnSave.setOnClickListener {
            if(viewModel.validateInput()) {
                setFragmentResult(resultKey, bundleOf("productAttribute" to viewModel.productAttribute))
                findNavController().popBackStack()
            }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) {
            toast(it)
        }
    }

    private fun onAddOption(option: ProductAttributeOption) {
        viewModel.addOptions(option)
        bindOptionsList()
    }

    private fun editOption(option: ProductAttributeOption) {
        createOptionDialog(option) {
            bindOptionsList()
        }
    }

    private fun delete(pos: Int, option: ProductAttributeOption) {
        viewModel.deleteOption(pos, option)
        bindOptionsList()
    }

    private fun setupData() {
        binding.productAttribute = viewModel.productAttribute
        bindOptionsList()
    }

    private fun bindOptionsList() {
        adapter.items = viewModel.productAttribute.options
        //adapter.notifyDataSetChanged()
    }

    private fun createOptionDialog(option: ProductAttributeOption?, positiveAction: (ProductAttributeOption) -> Unit) {
        val builder = AlertDialog.Builder(requireContext())

        if(option != null) {
            builder.setTitle("Chỉnh sửa")
        } else {
            builder.setTitle("Thêm mới lựa chọn")
        }

        val menuRoot: View = layoutInflater.inflate(R.layout.dialog_attribute_option, null)
        val edtOptionName = menuRoot.findViewById<EditText>(R.id.edt1)
        val edtOptionPrice = menuRoot.findViewById<EditText>(R.id.edt2)
        edtOptionName.hint = "Tên"
        edtOptionPrice.hint = "Giá"
        if (option?.name != null) edtOptionName.setText(option.name)
        if (option?.price != null) edtOptionPrice.setText(option.price.toString())
        builder.setView(menuRoot)
        builder.setPositiveButton("OK") { dialog, which ->
            if (edtOptionName.text.isNullOrEmpty() || edtOptionPrice.text.isNullOrEmpty()) {
                Toast.makeText(requireContext(),"Điền đầy đủ thông tin.", Toast.LENGTH_LONG).show()
            } else {
                var price = 0.0
                try {
                    price = edtOptionPrice.text.toString().toDouble()
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "Giá không hợp lệ", Toast.LENGTH_LONG).show()
                    return@setPositiveButton
                }

                val result = option ?: ProductAttributeOption()
                result.name = edtOptionName.text.toString()
                result.price = price
                positiveAction(result)
                dialog.cancel()
            }
        }
        builder.setNegativeButton("Huỷ") { dialog, which -> dialog.cancel() }
        builder.create().show()
    }

    private fun createItemPopupMenu(view: View, pos: Int,option: ProductAttributeOption) {
        val popup = PopupMenu(requireContext(), view)
        val inflater: MenuInflater = popup.getMenuInflater()
        inflater.inflate(R.menu.item_popup_common_menu, popup.getMenu())
        popup.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.item_edit_product_menu -> editOption(option)
                R.id.item_delete_item -> delete(pos, option)
            }

            return@setOnMenuItemClickListener true
        }
        popup.show()
    }

    companion object {
        const val resultKey = "resultKey"
    }
}