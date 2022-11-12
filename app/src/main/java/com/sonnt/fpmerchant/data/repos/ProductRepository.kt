package com.sonnt.fpmerchant.data.repos

import com.sonnt.fpmerchant.model.Product
import com.sonnt.fpmerchant.network.ApiResult
import com.sonnt.fpmerchant.network.NetworkModule
import com.sonnt.fpmerchant.network.callApi

class ProductRepository private constructor(){

    val productService = NetworkModule.productService

    suspend fun editProduct(product: Product): Boolean {
        val response = callApi { productService.editProduct(product) }
        return when(response) {
            is ApiResult.Success -> true
            is ApiResult.Failed -> false
        }
    }


    companion object {
        val shared = ProductRepository()
    }
}