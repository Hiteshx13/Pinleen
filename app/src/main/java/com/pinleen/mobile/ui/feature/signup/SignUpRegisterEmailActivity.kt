package com.pinleen.mobile.ui.feature.signup

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.appcompat.widget.AppCompatTextView
import com.pinleen.mobile.databinding.ActivitySignupBinding
import com.pinleen.mobile.ui.base.BaseActivity


class SignUpRegisterEmailActivity : BaseActivity<ActivitySignupBinding>() {
    private val signUpViewModel: SignUpViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initObserver()
    }

    override val bindingInflater: (LayoutInflater) -> ActivitySignupBinding
        get() = ActivitySignupBinding::inflate

    override fun initListener() {
        binding.btnRegister.setOnClickListener {
            //  signUpViewModel.registerEmailPassword(
//                    RequestRegisterEmail(
//                        binding.etEmail.text.toString(),
//                        binding.etPassword.text.toString()
//                    )
//                )
        }

        binding.etPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(password: Editable?) {
                signUpViewModel.validateEmailPassword(
                    binding.etEmail.text.toString(),
                    password.toString(), object : ValidEmailPasswordListener {
                        override fun isValid(
                            isValidEmail: Boolean,
                            isUppercase: Boolean,
                            isLowercase: Boolean,
                            isAnyNumber: Boolean,
                            isPasswordLengthValid: Boolean
                        ) {
                            changeState(isUppercase, binding.tvOneUpperCase)
                            changeState(isLowercase, binding.tvOneLowerCase)
                            changeState(isAnyNumber, binding.tvOneNumeric)
                            changeState(isPasswordLengthValid, binding.tvMinimumLength)

                            if (isValidEmail
                                && isUppercase
                                && isLowercase
                                && isAnyNumber
                                && isPasswordLengthValid) {
                                binding.btnRegister.isClickable=true
                                binding.btnRegister.alpha=1.0f

                            }else{
                                binding.btnRegister.isClickable=false
                                binding.btnRegister.alpha=0.5f
                            }
                        }
                    })
//            if (signUpViewModel.isValidData(
//                    binding.etEmail.text.toString(),
//                    binding.etPassword.text.toString()
//                )
//            ) {
//
//            }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    private fun changeState(isValid: Boolean, text: AppCompatTextView) {
        if (isValid) {
            text.setTextColor(Color.WHITE)
        } else {
            text.setTextColor(Color.RED)
        }
    }

    private fun initObserver() {
        signUpViewModel.responseRegister.observe(this,
            { response -> Log.d("PIK", "${response?.pik}") })

        signUpViewModel.responseRegisterEmail.observe(this,
            { response -> Log.d("response:", "$response") })
    }
}