package com.sonnt.fpmerchant.ui.productlist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sonnt.fpmerchant.data.repos.ProductRepository
import com.sonnt.fpmerchant.model.Product
import com.sonnt.fpmerchant.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class ProductListViewModel: BaseViewModel() {

    private val productRepository = ProductRepository.shared

    val listProduct = MutableLiveData<List<Product>>()

    fun getProductByMenu(menuId: Long) {
        viewModelScope.launch {
            listProduct.value = productRepository.getProductByMenu(menuId)
        }
    }

    fun deleteProduct(product: Product) {

    }

}