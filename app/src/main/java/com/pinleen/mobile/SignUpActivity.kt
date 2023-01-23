package com.pinleen.mobile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import com.pinleen.mobile.databinding.ActivitySignupBinding
import com.pinleen.mobile.ui.base.BaseActivity
import com.pinleen.mobile.utils.Constents


class SignUpActivity : BaseActivity<ActivitySignupBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnRegister.setOnClickListener {
            val email = binding.etEmail.text
            val intent = Intent(this, SignUpVerificationActivity::class.java)
            intent.putExtra(Constents.PARAM_EMAIL, email)
            startActivity(intent)
        }
    }

    override val bindingInflater: (LayoutInflater) -> ActivitySignupBinding
        get() = ActivitySignupBinding::inflate
}