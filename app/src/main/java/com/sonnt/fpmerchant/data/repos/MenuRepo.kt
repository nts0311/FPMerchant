package com.sonnt.fpmerchant.data.repos

import androidx.lifecycle.viewModelScope
import com.sonnt.fpmerchant.model.ProductMenu
import com.sonnt.fpmerchant.network.ApiResult
import com.sonnt.fpmerchant.network.NetworkModule
import com.sonnt.fpmerchant.network.callApi
import kotlinx.coroutines.launch

class MenuRepo private constructor() {

    var menus: MutableList<ProductMenu> = mutableListOf()
        private set

    suspend fun getMenuList(): List<ProductMenu> {

        val response = callApi { NetworkModule.menuService.getAllMenu() }

        return when (response) {
            is ApiResult.Success -> {
                menus = response.data?.menus?.toMutableList() ?: mutableListOf()
                menus
            }

            is ApiResult.Failed -> emptyList()
        }
    }

    suspend fun addMenu(name: String): Boolean {

        val menu = ProductMenu(name = name)
        val response = callApi { NetworkModule.menuService.addMenu(menu) }

        when (response) {
            is ApiResult.Success -> {
                val newMenu = response.data?.menu ?: return false
                menus.add(newMenu)
                return true
            }

            is ApiResult.Failed -> return false
        }
    }

    suspend fun editMenu(menu: ProductMenu): Boolean {

        val response = callApi { NetworkModule.menuService.editMenu(menu) }

        return when (response) {
            is ApiResult.Success -> {
                menus.first { it.id == menu.id }.name = menu.name
                true
            }

            is ApiResult.Failed -> false
        }

    }

    companion object {
        val shared = MenuRepo()
    }
}