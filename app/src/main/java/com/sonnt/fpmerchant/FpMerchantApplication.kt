package com.sonnt.fpmerchant

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.sonnt.fpmerchant.di.AppModule
import com.sonnt.fpmerchant.message.SessionExpiredEvent
import com.sonnt.fpmerchant.network.NetworkModule
import com.sonnt.fpmerchant.network.dto.request.UpdateFcmTokenRequest
import com.sonnt.fpmerchant.ui.auth.LoginActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
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

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
            Log.d("FCM", token)

            GlobalScope.launch {
                NetworkModule.authService.updateFcmToken(UpdateFcmTokenRequest(fcmToken = token))
            }
        })
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
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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