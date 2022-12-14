package com.sonnt.fpmerchant.ui.doneorders

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sonnt.fpmerchant.data.repos.OrderRepository
import com.sonnt.fpmerchant.message.WSConnectedEvent
import com.sonnt.fpmerchant.model.OrderInfo
import com.sonnt.fpmerchant.ui.base.BaseViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class DoneOrdersViewModel: BaseViewModel() {

    val doneOrders = MutableLiveData<List<OrderInfo>>()

    init {
        EventBus.getDefault().register(this)
    }

    @Subscribe
    fun onWSConnected(event: WSConnectedEvent?) {
        subscribeForOrderComplete()
    }

    fun getDoneOrders() {
        viewModelScope.launch {
            val doneOrdersList = OrderRepository.shared.getDoneOrders() ?: return@launch
            doneOrders.value = doneOrdersList
        }
    }

    private fun subscribeForOrderComplete() {
        OrderRepository.shared.getOrderCompletedFlow().onEach {
            doneOrders.value = OrderRepository.shared.doneOrder
        }.launchIn(viewModelScope)
    }

}