package com.sonnt.fpmerchant.ui.auth

import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.sonnt.fpmerchant.MainActivity
import com.sonnt.fpmerchant.R
import com.sonnt.fpmerchant.databinding.ActivityLoginBinding
import com.sonnt.fpmerchant.ui.auth.LoginViewModel
import com.sonnt.fpmerchant.utils.EventCode
import com.sonnt.fpmerchant.utils.EventHub

class LoginActivity : AppCompatActivity() {

    private val viewModel: LoginViewModel by viewModels()

    var isFromSplash = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        isFromSplash = intent.extras?.get("from_splash") as? Boolean ?: false

        val binding: ActivityLoginBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_login
        )

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        setStatusBarTransparent(this)

        viewModel.isLoginSuccess.observe(this) {isLoggedIn ->
            if (!isLoggedIn)
                return@observe

            EventHub.post(EventCode.loggedIn.rawValue)

            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)

            finish()
        }

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
    }

    private fun setStatusBarTransparent(activity: AppCompatActivity){
        //Make Status bar transparent
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
        //Make status bar icons color dark
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            activity.window.statusBarColor = Color.WHITE
        }
    }
}
