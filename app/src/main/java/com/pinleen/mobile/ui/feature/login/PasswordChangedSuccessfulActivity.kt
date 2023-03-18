package com.pinleen.mobile.ui.feature.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import com.pinleen.mobile.databinding.ActivityPasseordChangeSuccessBinding
import com.pinleen.mobile.ui.base.BaseActivity
import com.pinleen.mobile.ui.feature.dashboard.DashboardActivity


class PasswordChangedSuccessfulActivity : BaseActivity<ActivityPasseordChangeSuccessBinding>() {

    companion object {
        fun getIntent(mContext: Context): Intent {
            return Intent(mContext, PasswordChangedSuccessfulActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initObserver()
    }

    override val bindingInflater: (LayoutInflater) -> ActivityPasseordChangeSuccessBinding
        get() = ActivityPasseordChangeSuccessBinding::inflate

    override fun initListener() {

        binding.btnLogin.setOnClickListener {
            launchActivity(DashboardActivity.getIntent(this))
        }
    }


    private fun initObserver() {

    }
}