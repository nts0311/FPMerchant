package com.sonnt.fpmerchant.ui.auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sonnt.fpmerchant.data.local.AuthDataSource
import com.sonnt.fpmerchant.ui.base.BaseViewModel
import com.sonnt.fpmerchant.network.ApiResult
import com.sonnt.fpmerchant.network.NetworkModule
import com.sonnt.fpmerchant.network.callApi
import com.sonnt.fpmerchant.network.dto.request.AuthRequest
import com.sonnt.fpmerchant.network.dto.response.AuthenticationResponse
import kotlinx.coroutines.launch

class LoginViewModel: BaseViewModel() {

    var username: String = "comtho123"
    var password: String = "123456"

    var isLoginButtonEnabled = MutableLiveData(true)

    var isLoginSuccess = MutableLiveData(false)

    fun login() {
        viewModelScope.launch {
            loading(true)

            val authenticationResponse = callApi {
                NetworkModule.authService.login(AuthRequest(username, password))
            }
            loading(false)
            when(authenticationResponse) {
                is ApiResult.Success -> onLoginSuccess(authenticationResponse)
                is ApiResult.Failed -> onLoginFailed(authenticationResponse)
            }
        }
    }

    private fun onLoginSuccess(authResponse:  ApiResult.Success<AuthenticationResponse?>) {
        authResponse.data?.also {
            AuthDataSource.authToken = it.jwtToken
            AuthDataSource.name = it.name ?: ""
            AuthDataSource.avatarURL = it.avatarUrl ?: ""
            isLoginSuccess.value = true
        }
    }

    private fun onLoginFailed(authResponse: ApiResult.Failed<AuthenticationResponse?>?) {
        val errorMessage = authResponse?.error?.message ?: "Lỗi hệ thống, vui lòng thử lại sau!"
        error(errorMessage)
    }

    fun validateInput() {
        var isEnabled = true

        if(username == null || password == null || username?.isEmpty() == true || password?.isEmpty() == true)
            isEnabled = false

        if ((password.count()) < 6)
            isEnabled = false

        isLoginButtonEnabled.value = isEnabled
    }
}