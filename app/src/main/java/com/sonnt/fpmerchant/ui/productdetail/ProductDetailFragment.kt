package com.sonnt.fpmerchant.ui.productdetail

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.sonnt.fpmerchant.R
import com.sonnt.fpmerchant.databinding.FragmentProductDetailBinding
import com.sonnt.fpmerchant.databinding.ItemProductAttributeBinding
import com.sonnt.fpmerchant.model.Product
import com.sonnt.fpmerchant.model.ProductAttribute
import com.sonnt.fpmerchant.model.ProductCategory
import com.sonnt.fpmerchant.ui.base.BaseFragment
import com.sonnt.fpmerchant.ui.base.BaseRecyclerViewAdapter
import com.sonnt.fpmerchant.ui.productattribute.ProductAttributeFragment

class ProductDetailFragment : BaseFragment<FragmentProductDetailBinding>() {
    override var layoutResId: Int = R.layout.fragment_product_detail

    private val viewModel: ProductDetailViewModel by viewModels()
    private lateinit var attributesAdapter: BaseRecyclerViewAdapter<ProductAttribute, ItemProductAttributeBinding>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            viewModel.product = it.getParcelable("product") ?: Product(status = "AVAILABLE")
            viewModel.menuId = it.getLong("menuId")
        }
        setFragmentResultListener(ProductAttributeFragment.resultKey) { requestKey, bundle ->
            val attribute: ProductAttribute = bundle.getParcelable("productAttribute") ?: return@setFragmentResultListener
            viewModel.onAddOrEditAttribute(attribute)
            bindListAttrs()
        }
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        bindViewModel()
        setupData()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.fragment_product_detail_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.save) {
            viewModel.saveProduct()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            val uri: Uri = data?.data!!

            Glide.with(this)
                .load(uri)
                .into(binding.imgProduct)
            viewModel.uploadProductImage(uri)
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {

        }
    }

    private fun setupViews() {
        val title = if(viewModel.isCreatingNewProduct) "Thêm sản phẩm" else "Chi tiết sản phẩm"
        setActionBarTitle(title)
        attributesAdapter = BaseRecyclerViewAdapter(R.layout.item_product_attribute)
        attributesAdapter.onItemLongClick = { view, i, productAttributeOption ->
            createItemPopupMenu(view, i,productAttributeOption)
        }
        attributesAdapter.onItemClicked = { i, attribute ->
            editAttribute(attribute)
        }

        binding.apply {
            rvAttributes.adapter=attributesAdapter
            rvAttributes.layoutManager = LinearLayoutManager(requireContext())

            buttonAddAttrribute.setOnClickListener {
                toAttributeDetailScreen(null)
            }

            switchLayout.setOnClickListener {
                val isAvailableNow = !switchStatus.isChecked
                switchStatus.isChecked = isAvailableNow
                this@ProductDetailFragment.viewModel.setProductAvailability(isAvailableNow)
            }

            imgProduct.setOnClickListener {
                ImagePicker.with(this@ProductDetailFragment)
                    .start()
            }

            edtPrice.addTextChangedListener {
                val price = it.toString().replace(",","")
                this@ProductDetailFragment.viewModel.product.price = if(price.isEmpty()) 0.0 else price.toDouble()
            }
        }
    }

    private fun bindViewModel() {
        viewModel.categories.observe(viewLifecycleOwner) {
            setupCategorySpinner(it)
            binding.spinner.setSelection(viewModel.getCategoriesPos())
        }

        viewModel.result.observe(viewLifecycleOwner) {isSuccess ->
            if (viewModel.isCreatingNewProduct) {
                setFragmentResult(ProductAttributeFragment.resultKey, bundleOf("changed" to true))
                findNavController().popBackStack()
            } else {
                val msg = if (isSuccess) "Cập nhập thay đổi thành công!" else "Cập nhập thay đổi thất bại!"
                toast(msg)
                setFragmentResult(ProductAttributeFragment.resultKey, bundleOf("changed" to true))
                findNavController().popBackStack()
            }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) {
            toast(it)
        }
    }

    private fun setupData() {
        binding.viewModel = viewModel
        viewModel.getCategory()
        setupProductData()
    }

    private fun bindListAttrs() {
        attributesAdapter.items = viewModel.product.attributes
    }

    private fun setupProductData() {
        val product = viewModel.product

        if (viewModel.isCreatingNewProduct) return

        bindListAttrs()

        binding.switchStatus.isChecked = product.isAvailable
        binding.edtPrice.setText(product.price.toString())

    }

    private fun setupCategorySpinner(categories: List<ProductCategory>) {
        val adapter = ArrayAdapter(requireContext(), R.layout.item_single_text, categories.map { it.name })
        binding.spinner.adapter = adapter

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                viewModel.setCategory(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
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

    companion object {
        const val resultKey = "resultKey"
    }
}