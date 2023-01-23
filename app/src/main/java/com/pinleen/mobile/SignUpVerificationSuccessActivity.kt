package com.pinleen.mobile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import com.pinleen.mobile.databinding.ActivitySignupVerificationSuccessBinding
import com.pinleen.mobile.ui.base.BaseActivity


class SignUpVerificationSuccessActivity : BaseActivity<ActivitySignupVerificationSuccessBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.btnGetStarted.setOnClickListener {
            val intent = Intent(this, FaceVerificationActivity::class.java)
            startActivity(intent)
        }
    }

    override val bindingInflater: (LayoutInflater) -> ActivitySignupVerificationSuccessBinding
        get() = ActivitySignupVerificationSuccessBinding::inflate
}