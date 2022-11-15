package com.sonnt.fpmerchant.data.repos

import com.sonnt.fpmerchant.model.Product
import com.sonnt.fpmerchant.network.ApiResult
import com.sonnt.fpmerchant.network.NetworkModule
import com.sonnt.fpmerchant.network.callApi

class ProductRepository private constructor(){

    private val productService = NetworkModule.productService

    private val productList = mutableMapOf<Long, MutableList<Product>>()

    suspend fun editProduct(product: Product): Boolean {
        val response = callApi { productService.editProduct(product) }
        return when(response) {
            is ApiResult.Success -> {

                val productList = this.productList[product.tagId] ?: return true
                val index = productList.indexOfFirst { it.id == product.id }
                if (index != -1) {
                    productList[index] = product
                }

                true
            }
            is ApiResult.Failed -> false
        }
    }

    suspend fun addProduct(product: Product): Boolean {
        val response = callApi { productService.addProduct(product) }
        return when(response) {
            is ApiResult.Success -> {
                val product = response.data?.product ?: return true
                val productList = this.productList[product.tagId] ?: return true
                productList.add(product)

                true
            }
            is ApiResult.Failed -> false
        }
    }

    suspend fun getProductByMenu(menuId: Long): List<Product> {
        if (productList[menuId] != null) {
            return productList[menuId] ?: listOf()
        }

        val response = callApi { NetworkModule.productService.getProductByMenu(menuId) }

        return when (response) {
            is ApiResult.Success -> {
                val products = response.data?.products ?: listOf()
                productList[menuId] = products.toMutableList()
                products
            }

            is ApiResult.Failed -> emptyList()
        }

    }


    companion object {
        val shared = ProductRepository()
    }
}