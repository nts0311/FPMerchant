package com.sonnt.fpmerchant

import android.app.Application

class FpMerchantApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: FpMerchantApplication
    }
}