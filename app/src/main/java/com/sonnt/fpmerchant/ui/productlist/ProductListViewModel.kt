package com.sonnt.fpmerchant.ui.productlist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sonnt.fpmerchant.data.repos.OrderRepository
import com.sonnt.fpmerchant.model.Product
import com.sonnt.fpmerchant.network.ApiResult
import com.sonnt.fpmerchant.network.NetworkModule
import com.sonnt.fpmerchant.network.callApi
import com.sonnt.fpmerchant.ui._base.BaseViewModel
import kotlinx.coroutines.launch

class ProductListViewModel: BaseViewModel() {

    val listProduct = MutableLiveData<List<Product>>()

    fun getProductByMenu(menuId: Long) {
        viewModelScope.launch {
            val response = callApi { NetworkModule.productService.getProductByMenu(menuId) }

            when (response) {
                is ApiResult.Success -> {
                    val products = response.data?.products ?: listOf()
                    listProduct.value = products
                }

                is ApiResult.Failed -> {}
            }
        }
    }

}