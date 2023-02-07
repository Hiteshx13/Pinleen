package com.pinleen.mobile.ui.feature.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.widget.AppCompatEditText
import com.pinleen.mobile.R
import com.pinleen.mobile.databinding.ActivitySetNewPasswordBinding
import com.pinleen.mobile.ui.base.BaseActivity


class SetNewPasswordActivity : BaseActivity<ActivitySetNewPasswordBinding>() {

    companion object {
        fun getIntent(mContext: Context): Intent {
            return Intent(mContext, SetNewPasswordActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initObserver()
    }

    override val bindingInflater: (LayoutInflater) -> ActivitySetNewPasswordBinding
        get() = ActivitySetNewPasswordBinding::inflate

    override fun initListener() {

        binding.ivback.setOnClickListener {
            finish()
        }
        binding.btnChangePassword.setOnClickListener {
            launchActivity(PasswordChangedSuccessfulActivity.getIntent(this))
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