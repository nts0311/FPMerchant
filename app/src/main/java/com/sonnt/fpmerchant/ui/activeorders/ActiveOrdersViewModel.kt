package com.sonnt.fpmerchant.ui.activeorders

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sonnt.fpmerchant.data.repos.OrderRepository
import com.sonnt.fpmerchant.model.OrderInfo
import com.sonnt.fpmerchant.ui._base.BaseViewModel
import com.sonnt.fpmerchant.utils.EventCode
import com.sonnt.fpmerchant.utils.EventHub
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class ActiveOrdersViewModel: BaseViewModel() {

    val activeOrders = MutableLiveData<List<OrderInfo>>()

    init {
        EventHub.subscribe(EventCode.connectedWS.rawValue, viewModelScope) {
            subscribeForNewOrder()
        }
    }

    fun getActiveOrders() {
        viewModelScope.launch {
            val activeOrderList = OrderRepository.shared.getActiveOrders() ?: return@launch
            activeOrders.value = activeOrderList
        }
    }

    private fun subscribeForNewOrder() {
        OrderRepository.shared.newOrderRequestFlow?.onEach {
            activeOrders.value = OrderRepository.shared.activeOrders
        }?.launchIn(viewModelScope)
    }

}