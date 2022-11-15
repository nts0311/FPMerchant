package com.sonnt.fpmerchant.ui.merchantinfo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sonnt.fpmerchant.network.ApiResult
import com.sonnt.fpmerchant.network.NetworkModule
import com.sonnt.fpmerchant.network.callApi
import com.sonnt.fpmerchant.network.dto.request.ChangeMerchantActiveHourRequest
import com.sonnt.fpmerchant.network.dto.request.ChangeMerchantActivityStatusRequest
import com.sonnt.fpmerchant.network.dto.response.GetMerchantInfoResponse
import com.sonnt.fpmerchant.ui.base.BaseViewModel
import kotlinx.coroutines.launch
import java.time.LocalTime

class MerchantInfoViewModel: BaseViewModel() {

    val merchantInfo = MutableLiveData<GetMerchantInfoResponse>()
    val changeActivityStatus = MutableLiveData<Boolean>()
    val changeActiveHour = MutableLiveData<ChangeMerchantActiveHourRequest>()

    fun getMerchantInfo() {
        viewModelScope.launch {
            val response = callApi { NetworkModule.infoService.getMerchantInfo() }

            when (response) {
                is ApiResult.Success -> {
                    merchantInfo.value = response.data ?: return@launch
                }

                is ApiResult.Failed -> {
                    error("Lỗi khi lấy thông tin nhà hàng")
                }
            }
        }
    }

    fun changeActivityStatus(isOpen: Boolean) {
        viewModelScope.launch {
            val request = ChangeMerchantActivityStatusRequest(isOpen)
            val response = callApi { NetworkModule.infoService.changeActivityStatus(request) }

            when (response) {
                is ApiResult.Success -> {
                    changeActivityStatus.value = true
                }

                is ApiResult.Failed -> {
                    error("Lỗi khi cập nhập thông tin")
                }
            }
        }
    }

    fun changeActiveHour(open: LocalTime?, close: LocalTime?) {
        viewModelScope.launch {
            val request = ChangeMerchantActiveHourRequest(
                open?.toString(),
                close?.toString()
            )
            val response = callApi { NetworkModule.infoService.changeActiveHour(request) }

            when (response) {
                is ApiResult.Success -> {
                    changeActiveHour.value = request
                }

                is ApiResult.Failed -> {
                    error("Lỗi khi cập nhập thông tin")
                }
            }
        }
    }



}