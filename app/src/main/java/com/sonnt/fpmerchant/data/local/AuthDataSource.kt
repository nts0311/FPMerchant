package com.sonnt.fpmerchant.data.local

import com.sonnt.fpmerchant.di.AppModule

object AuthDataSource {

    private val sharedPreferencesApi = AppModule.provideSharedPreferencesApi()

    var authToken: String
        get() = sharedPreferencesApi.get(AUTH_TOKEN, String::class.java)
        set(value) {
            sharedPreferencesApi.put(AUTH_TOKEN, value)
        }

    private const val AUTH_TOKEN = "AUTH_TOKEN"
    private const val USER_INFO = "USER_INFO"
    private const val IS_REMEMBER_LOGIN_INFO = "IS_REMEMBER_LOGIN_INFO"
    private const val USER_LOGIN_INFO = "USER_LOGIN_INFO"
}