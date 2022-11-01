package com.sonnt.fpmerchant

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import com.sonnt.fpmerchant.di.AppModule
import com.sonnt.fpmerchant.message.SessionExpiredEvent
import com.sonnt.fpmerchant.ui.auth.LoginActivity
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class FpMerchantApplication: Application() {

    lateinit var currentActivity: Activity

    override fun onCreate() {
        super.onCreate()
        instance = this
        setActivitiesListener()
        EventBus.getDefault().register(this)
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: SessionExpiredEvent) {
        showLoginScreen()
    }

    companion object {
        lateinit var instance: FpMerchantApplication
    }
}