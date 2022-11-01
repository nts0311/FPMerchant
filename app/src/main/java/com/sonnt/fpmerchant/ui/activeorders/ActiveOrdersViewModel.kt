package com.sonnt.fpmerchant.ui.activeorders

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sonnt.fpmerchant.data.repos.OrderRepository
import com.sonnt.fpmerchant.message.WSConnectedEvent
import com.sonnt.fpmerchant.model.OrderInfo
import com.sonnt.fpmerchant.ui._base.BaseViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class ActiveOrdersViewModel: BaseViewModel() {

    val activeOrders = MutableLiveData<List<OrderInfo>>()

    init {
        EventBus.getDefault().register(this)
    }

    @Subscribe
    fun onWSConnected(event: WSConnectedEvent?) {
        subscribeForNewOrder()
    }

    fun getActiveOrders() {
        viewModelScope.launch {
            val activeOrderList = OrderRepository.shared.getActiveOrders() ?: return@launch
            activeOrders.value = activeOrderList
        }
    }

    private fun subscribeForNewOrder() {
        OrderRepository.shared.getNewOrderRequestFlow().onEach {
            activeOrders.value = OrderRepository.shared.activeOrders
        }.launchIn(viewModelScope)
    }

}