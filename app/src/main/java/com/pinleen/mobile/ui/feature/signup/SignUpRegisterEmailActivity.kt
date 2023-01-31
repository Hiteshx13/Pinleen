package com.pinleen.mobile.ui.feature.signup

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.appcompat.widget.AppCompatTextView
import com.pinleen.mobile.R
import com.pinleen.mobile.data.models.request.RequestRegisterEmail
import com.pinleen.mobile.databinding.ActivitySignupBinding
import com.pinleen.mobile.ui.base.BaseActivity
import com.pinleen.mobile.utils.showMessageDialog


class SignUpRegisterEmailActivity : BaseActivity<ActivitySignupBinding>() {
    private val signUpViewModel: SignUpViewModel by viewModels()
    private var PIK = ""
    val mapAuth = HashMap<String, String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initObserver()
    }

    override val bindingInflater: (LayoutInflater) -> ActivitySignupBinding
        get() = ActivitySignupBinding::inflate

    override fun initListener() {
        binding.btnRegister.setOnClickListener {
            validateAndCallSignUp()
        }
    }

    private fun validateAndCallSignUp() {
        signUpViewModel.validateEmailPassword(
            binding.etEmail.text.toString(),
            binding.etPassword.text.toString(), object : ValidEmailPasswordListener {
                override fun isValid(
                    isValidEmail: Boolean,
                    isUppercase: Boolean,
                    isLowercase: Boolean,
                    isAnyNumber: Boolean,
                    isPasswordLengthValid: Boolean
                ) {
//                    changeState(isUppercase, binding.tvOneUpperCase)
//                    changeState(isLowercase, binding.tvOneLowerCase)
//                    changeState(isAnyNumber, binding.tvOneNumeric)
//                    changeState(isPasswordLengthValid, binding.tvMinimumLength)

                    if (isValidEmail
                        && isUppercase
                        && isLowercase
                        && isAnyNumber
                        && isPasswordLengthValid
                    ) {
                        mapAuth["cu-x-server"] = "8jfy572hf74xfhhg23C343u5u2jfw3240"
                        mapAuth["Content-Type"] = "application/json"
                        mapAuth["Authorization"] = "Bearer $PIK"

                        signUpViewModel.registerEmailPassword(
                            RequestRegisterEmail(
                                binding.etEmail.text.toString(),
                                binding.etPassword.text.toString()
                            ),
                            mapAuth
                        )

                    } else if (!isValidEmail) {
                        showMessageDialog(
                            this@SignUpRegisterEmailActivity,
                            getString(R.string.please_enter_valid_email)
                        )
                    }else if (!isUppercase || !isLowercase || !isAnyNumber || !isPasswordLengthValid) {
                        showMessageDialog(
                            this@SignUpRegisterEmailActivity,
                            getString(R.string.please_enter_valid_password)
                        )
                    } else if (!binding.cbAge.isChecked) {
                        showMessageDialog(
                            this@SignUpRegisterEmailActivity,
                            getString(R.string.please_check) + "" + getString(R.string.i_m_at_least_18_years_old_and_agree_to_the_following_terms)
                        )
                    } else if (!binding.cbAgreement.isChecked) {
                        showMessageDialog(
                            this@SignUpRegisterEmailActivity,
                            getString(R.string.please_check) + "" + getString(R.string.i_ve_read_and_agree_to_the_e_sign_disclosure_and_consent_to_receive_all_communications_electronically)
                        )
                    }
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
            { response ->
                if (response.isSuccessful) {
                    Log.d("PIK", "${response?.body()?.pik}")
                    PIK = response?.body()?.pik ?: ""
                } else {
                    showMessageDialog(this, response?.message() ?: "")
                }
            })

        signUpViewModel.responseRegisterEmail.observe(this,
            { response ->
                if (response.isSuccessful) {
                    Log.d("PIK", "${response?.body()?.pik}")
                    PIK = response?.body()?.pik ?: ""
                    launchActivity( SignUpVerificationActivity.getIntent(this))
                } else {
                    showMessageDialog(this, response?.message() ?: "")
                }
            })
    }
}