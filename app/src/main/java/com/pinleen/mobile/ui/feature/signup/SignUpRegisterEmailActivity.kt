package com.pinleen.mobile.ui.feature.signup

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.activity.viewModels
import com.pinleen.mobile.data.models.request.RequestRegisterEmail
import com.pinleen.mobile.databinding.ActivitySignupBinding
import com.pinleen.mobile.ui.base.BaseActivity


class SignUpRegisterEmailActivity : BaseActivity<ActivitySignupBinding>() {
    val signUpViewModel: SignUpViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initObserver()
    }

    override val bindingInflater: (LayoutInflater) -> ActivitySignupBinding
        get() = ActivitySignupBinding::inflate

    override fun initClickListener() {
        binding.btnRegister.setOnClickListener {
            if (signUpViewModel.isValidData(
                    binding.etEmail.text.toString(),
                    binding.etPassword.text.toString()
                )
            ) {
                signUpViewModel.registerEmailPassword(
                    RequestRegisterEmail(
                        binding.etEmail.text.toString(),
                        binding.etPassword.text.toString()
                    )
                )
            }

        }
    }

    private fun initObserver() {
        signUpViewModel.responseRegister.observe(this,
            { response -> Log.d("PIK", "${response?.pik}") })

        signUpViewModel.responseRegisterEmail.observe(this,
            { response -> Log.d("response:", "$response") })
    }
}