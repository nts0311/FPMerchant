package com.sonnt.fpmerchant.ui.menu

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sonnt.fpmerchant.model.ProductMenu
import com.sonnt.fpmerchant.network.ApiResult
import com.sonnt.fpmerchant.network.NetworkModule
import com.sonnt.fpmerchant.network.callApi
import com.sonnt.fpmerchant.ui._base.BaseViewModel
import kotlinx.coroutines.launch

class ProductMenuListViewModel: BaseViewModel() {

    val menus = MutableLiveData<List<ProductMenu>>()

    fun getMenuList() {
        viewModelScope.launch {
            val response = callApi { NetworkModule.menuService.getAllMenu() }

            when (response) {
                is ApiResult.Success -> {
                    menus.value = response.data?.menus
                }

                is ApiResult.Failed -> {}
            }
        }
    }

    fun addMenu(name: String) {

        viewModelScope.launch {
            val menu = ProductMenu(name = name)
            val response = callApi { NetworkModule.menuService.addMenu(menu) }

            when (response) {
                is ApiResult.Success -> {
                    val newMenu = response.data?.menu ?: return@launch
                    val newListMenu = menus.value?.toMutableList() ?: return@launch
                    newListMenu.add(newMenu)
                    menus.value = newListMenu
                }

                is ApiResult.Failed -> {}
            }
        }
    }

    fun editMenu(menu: ProductMenu) {

        viewModelScope.launch {
            val response = callApi { NetworkModule.menuService.editMenu(menu) }

            when (response) {
                is ApiResult.Success -> {
                    val menuList = menus.value?.toMutableList() ?: return@launch
                    menuList.first { it.id == menu.id }.name = menu.name
                    menus.value = menuList
                }

                is ApiResult.Failed -> {}
            }
        }
    }

}