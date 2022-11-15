package com.sonnt.fpmerchant.ui.menu

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sonnt.fpmerchant.data.repos.MenuRepo
import com.sonnt.fpmerchant.model.ProductMenu
import com.sonnt.fpmerchant.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class ProductMenuListViewModel: BaseViewModel() {

    val menus = MutableLiveData<List<ProductMenu>>()
    val menuRepo = MenuRepo.shared

    fun getMenuList() {
        viewModelScope.launch {
            menus.value = menuRepo.getMenuList()
        }
    }

    fun addMenu(name: String) {
        viewModelScope.launch {
            val isSuccess = menuRepo.addMenu(name)

            if (isSuccess) {
                menus.value = menuRepo.menus
            } else {
                error("Lỗi khi thêm menu")
            }
        }
    }

    fun editMenu(menu: ProductMenu) {

        viewModelScope.launch {
            val isSuccess = menuRepo.editMenu(menu)

            if (isSuccess) {
                menus.value = menuRepo.menus
            } else {
                error("Lỗi khi sửa menu")
            }
        }
    }

}