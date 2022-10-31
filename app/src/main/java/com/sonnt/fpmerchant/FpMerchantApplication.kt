package com.sonnt.fpmerchant

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import com.sonnt.fpmerchant.di.AppModule
import com.sonnt.fpmerchant.ui.auth.LoginActivity
import com.sonnt.fpmerchant.utils.EventCode
import com.sonnt.fpmerchant.utils.EventHub

class FpMerchantApplication: Application() {

    lateinit var currentActivity: Activity

    override fun onCreate() {
        super.onCreate()
        instance = this
        setActivitiesListener()
        subscribeForAppEvents()
    }

    fun setActivitiesListener() {
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(
                activity: Activity,
                savedInstanceState: Bundle?
            ) {
                currentActivity = activity
            }

            override fun onActivityStarted(activity: Activity) {
                currentActivity = activity
            }
            override fun onActivityResumed(activity: Activity) {
                currentActivity = activity
            }
            override fun onActivityPaused(activity: Activity) {}
            override fun onActivityStopped(activity: Activity) {}
            override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle) {}
            override fun onActivityDestroyed(activity: Activity) {}

        })
    }

    fun showLoginScreen() {
        val intent = Intent(instance, LoginActivity::class.java)
        startActivity(intent)
    }

    fun setUpApplicationAfterLoggingIn() {
        AppModule.provideStompMessageHub().reconnect()
    }

    fun subscribeForAppEvents() {
        EventHub.subscribe(EventCode.sessionExpired.rawValue, AppModule.provideAppCoroutineScope()) {
            showLoginScreen()
        }
    }

    companion object {
        lateinit var instance: FpMerchantApplication
    }
}