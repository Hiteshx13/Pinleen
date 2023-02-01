package com.pinleen.mobile.ui.feature.signup

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.appcompat.widget.AppCompatTextView
import com.pinleen.mobile.R
import com.pinleen.mobile.data.models.request.RequestRegisterEmail
import com.pinleen.mobile.databinding.ActivityRegisterEmailBinding
import com.pinleen.mobile.ui.base.BaseActivity
import com.pinleen.mobile.utils.Constants.PARAM_EMAIL
import com.pinleen.mobile.utils.Constants.PARAM_PIK
import com.pinleen.mobile.utils.showMessageDialog


class RegisterEmailActivity : BaseActivity<ActivityRegisterEmailBinding>() {
    private val signUpViewModel: SignUpViewModel by viewModels()
    private var PIK = ""
    val mapAuth = HashMap<String, String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initObserver()
        signUpViewModel.register()
    }

    override val bindingInflater: (LayoutInflater) -> ActivityRegisterEmailBinding
        get() = ActivityRegisterEmailBinding::inflate

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
                    val isAgeChecked = binding.cbAge.isChecked
                    val isAgreement = binding.cbAgreement.isChecked
                    if (isValidEmail
                        && isUppercase
                        && isLowercase
                        && isAnyNumber
                        && isPasswordLengthValid
                        && isAgeChecked
                        && isAgreement
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
                            this@RegisterEmailActivity,
                            getString(R.string.please_enter_valid_email)
                        )
                    } else if (!isUppercase || !isLowercase || !isAnyNumber || !isPasswordLengthValid) {
                        showMessageDialog(
                            this@RegisterEmailActivity,
                            getString(R.string.please_enter_valid_password)
                        )
                    } else if (!isAgeChecked) {
                        showMessageDialog(
                            this@RegisterEmailActivity,
                            getString(R.string.please_check) + "" + getString(R.string.i_m_at_least_18_years_old_and_agree_to_the_following_terms)
                        )
                    } else if (!isAgreement) {
                        showMessageDialog(
                            this@RegisterEmailActivity,
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
                    PIK = response?.body()?.pik ?: ""
                } else {
                    showMessageDialog(this, response?.message() ?: "")
                }
            })

        signUpViewModel.responseRegisterEmail.observe(this,
            { response ->
                if (response.isSuccessful) {
                    PIK = response?.body()?.pik ?: ""
                    val intent = VerifyEmailOTPActivity.getIntent(this)
                    intent.putExtra(PARAM_EMAIL, binding.etEmail.text)
                    intent.putExtra(PARAM_PIK, PIK)
                    launchActivity(intent)
                } else {
                    showMessageDialog(this, response?.message() ?: "")
                }
            })
    }
}