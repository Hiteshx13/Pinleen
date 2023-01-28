package com.pinleen.mobile.ui.feature.signup

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import com.pinleen.mobile.FaceVerificationActivity
import com.pinleen.mobile.databinding.ActivitySignupVerificationSuccessBinding
import com.pinleen.mobile.ui.base.BaseActivity


class SignUpVerificationSuccessActivity : BaseActivity<ActivitySignupVerificationSuccessBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override val bindingInflater: (LayoutInflater) -> ActivitySignupVerificationSuccessBinding
        get() = ActivitySignupVerificationSuccessBinding::inflate

    override fun initListener() {
        binding.btnGetStarted.setOnClickListener {
            val intent = Intent(this, FaceVerificationActivity::class.java)
            startActivity(intent)
        }
    }
}