package com.sonnt.fpmerchant.di

import com.google.gson.Gson
import com.sonnt.fpmerchant.data.local.SharedPreferencesApi
import com.sonnt.fpmerchant.network.stomp.StompMessageHub
import com.sonnt.fpmerchant.FpMerchantApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

object AppModule {
    private val sharedPreferencesApi by lazy { SharedPreferencesApi() }

    private val gson by lazy { Gson() }

    private val stompMessageHub = StompMessageHub()

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun provideStompMessageHub() = stompMessageHub

    fun provideGson() = gson

    fun provideApplicationContext() = FpMerchantApplication.instance

    fun provideSharedPreferencesApi() = sharedPreferencesApi

    fun provideAppCoroutineScope() = coroutineScope
}