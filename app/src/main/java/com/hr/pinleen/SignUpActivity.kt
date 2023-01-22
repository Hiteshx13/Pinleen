package com.hr.pinleen

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import com.hr.pinleen.databinding.ActivitySignupBinding
import com.hr.pinleen.ui.base.BaseActivity
import com.hr.pinleen.utils.Constents


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