package com.pinleen.mobile

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import com.pinleen.mobile.databinding.ActivitySplashBinding
import com.pinleen.mobile.ui.base.BaseActivity
import com.pinleen.mobile.ui.feature.login.DashboardActivity
import com.pinleen.mobile.ui.feature.login.LoginActivity
import com.pinleen.mobile.utils.Constants
import com.pinleen.mobile.utils.PreferenceHelper

class SplashActivity : BaseActivity<ActivitySplashBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Handler(Looper.getMainLooper()).postDelayed({

            when {
                PreferenceHelper.getBoolean(this, Constants.PREF_IS_LOGGED_IN) -> {
                    launchActivity(DashboardActivity.getIntent(this))
                    finish()
                }
                PreferenceHelper.getBoolean(this, Constants.PREF_IS_PRIVACY_POLICY_SHOWN) -> {
                    launchActivity(LoginActivity.getIntent(this))
                    finish()
                }
                else -> {
                    launchActivity(PrivacyPolicyActivity.getIntent(this))
                    finish()
                }
            }

        }, 3000)
    }

    override val bindingInflater: (LayoutInflater) -> ActivitySplashBinding
        get() = ActivitySplashBinding::inflate


    override fun initListener() {

    }
}