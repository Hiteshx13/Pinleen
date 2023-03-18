package com.pinleen.mobile.ui.feature.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.widget.AppCompatEditText
import com.pinleen.mobile.R
import com.pinleen.mobile.databinding.ActivityChooseVerificationBinding
import com.pinleen.mobile.ui.base.BaseActivity


class ChooseVerificationActivity : BaseActivity<ActivityChooseVerificationBinding>() {

    companion object {
        fun getIntent(mContext: Context): Intent {
            return Intent(mContext, ChooseVerificationActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initObserver()
    }

    override val bindingInflater: (LayoutInflater) -> ActivityChooseVerificationBinding
        get() = ActivityChooseVerificationBinding::inflate

    override fun initListener() {

        binding.llButtonNext.setOnClickListener {
        launchActivity(VerifyVerificationOTPActivity.getIntent(this))
        }
        binding.etEmail.setOnClickListener {
            setState(binding.etEmail)
        }
        binding.etMobile.setOnClickListener {
            setState(binding.etMobile)
        }

    }


    private fun setState(view: AppCompatEditText) {
        binding.etMobile.isSelected = false
        binding.etEmail.isSelected = false

        binding.etMobile.compoundDrawables[0].setTint(resources.getColor(R.color.white))
        binding.etEmail.compoundDrawables[0].setTint(resources.getColor(R.color.white))

        view.isSelected = true

        view.compoundDrawables[0].setTint(resources.getColor(R.color.colorPurple))

    }

    private fun initObserver() {

    }
}