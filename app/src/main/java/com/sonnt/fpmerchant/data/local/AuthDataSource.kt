package com.sonnt.fpmerchant.data.local

import com.sonnt.fpmerchant.di.AppModule

object AuthDataSource {

    private val sharedPreferencesApi = AppModule.provideSharedPreferencesApi()

    var authToken: String
        get() = sharedPreferencesApi.get(AUTH_TOKEN, String::class.java)
        set(value) {
            sharedPreferencesApi.put(AUTH_TOKEN, value)
        }

    var name: String
        get() = sharedPreferencesApi.get(MERCHANT_NAME, String::class.java)
        set(value) {
            sharedPreferencesApi.put(MERCHANT_NAME, value)
        }

    var avatarURL: String
        get() = sharedPreferencesApi.get(AVATAR_URL, String::class.java)
        set(value) {
            sharedPreferencesApi.put(AVATAR_URL, value)
        }

    private const val AUTH_TOKEN = "AUTH_TOKEN"
    private const val MERCHANT_NAME = "MERCHANT_NAME"
    private const val AVATAR_URL = "AVATAR_URL"
    private const val USER_INFO = "USER_INFO"
    private const val IS_REMEMBER_LOGIN_INFO = "IS_REMEMBER_LOGIN_INFO"
    private const val USER_LOGIN_INFO = "USER_LOGIN_INFO"
}