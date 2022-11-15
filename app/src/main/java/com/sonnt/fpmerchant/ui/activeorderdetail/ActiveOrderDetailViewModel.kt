package com.sonnt.fpmerchant.ui.activeorderdetail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sonnt.fpmerchant.data.repos.OrderRepository
import com.sonnt.fpmerchant.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class ActiveOrderDetailViewModel: BaseViewModel() {

    val cancelOrderSuccess = MutableLiveData<Boolean>()

    fun cancelOrder(orderId: Long) {
        viewModelScope.launch {
            cancelOrderSuccess.value = OrderRepository.shared.cancelOrder(orderId)
        }
    }
}