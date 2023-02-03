package com.pinleen.mobile.ui.feature.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import com.pinleen.mobile.databinding.ActivityForgotPasswordBinding
import com.pinleen.mobile.ui.base.BaseActivity


class ForgotPasswordActivity : BaseActivity<ActivityForgotPasswordBinding>() {

    companion object {
        fun getIntent(mContext: Context): Intent {
            return Intent(mContext, ForgotPasswordActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initObserver()
    }

    override val bindingInflater: (LayoutInflater) -> ActivityForgotPasswordBinding
        get() = ActivityForgotPasswordBinding::inflate

    override fun initListener() {

        binding.llButtonNext.setOnClickListener {
            launchActivity(ChooseVerificationActivity.getIntent(this))
        }
    }


    private fun initObserver() {

    }
}