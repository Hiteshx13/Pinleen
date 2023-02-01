package com.pinleen.mobile.ui.feature.login

import android.os.Bundle
import android.view.LayoutInflater
import com.pinleen.mobile.databinding.ActivityForgotPasswordBinding
import com.pinleen.mobile.ui.base.BaseActivity


class ForgotPasswordActivity : BaseActivity<ActivityForgotPasswordBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initObserver()
    }

    override val bindingInflater: (LayoutInflater) -> ActivityForgotPasswordBinding
        get() = ActivityForgotPasswordBinding::inflate

    override fun initListener() {

    }


    private fun initObserver() {

    }
}