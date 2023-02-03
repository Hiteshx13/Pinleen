package com.pinleen.mobile.ui.feature.login

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import com.pinleen.mobile.databinding.ActivityLoginBinding
import com.pinleen.mobile.ui.base.BaseActivity
import com.pinleen.mobile.ui.feature.signup.SignUpViewModel


class LoginActivity : BaseActivity<ActivityLoginBinding>() {
    private val viewModel: LoginViewModel by viewModels()

    val mapAuth = HashMap<String, String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initObserver()
    }

    override val bindingInflater: (LayoutInflater) -> ActivityLoginBinding
        get() = ActivityLoginBinding::inflate

    override fun initListener() {
        binding.btnSignIn.setOnClickListener {

        }
        binding.tvForgotPassword.setOnClickListener {

            launchActivity(ForgotPasswordActivity.getIntent(this))
        }
        binding.tvSignUp.setOnClickListener {

        }


    }


    private fun initObserver() {

    }
}