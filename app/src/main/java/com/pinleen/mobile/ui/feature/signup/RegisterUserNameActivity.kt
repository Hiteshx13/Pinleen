package com.pinleen.mobile.ui.feature.signup

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import com.pinleen.mobile.data.models.request.RequestRegisterNameAndPhone
import com.pinleen.mobile.databinding.ActivityRegisterUserNameBinding
import com.pinleen.mobile.ui.base.BaseActivity
import com.pinleen.mobile.utils.Constants
import com.pinleen.mobile.utils.showMessageDialog


class RegisterUserNameActivity : BaseActivity<ActivityRegisterUserNameBinding>() {

    companion object {
        fun getIntent(mContext: Context): Intent {
            return Intent(mContext, RegisterUserNameActivity::class.java)
        }
    }

    private val viewModel: SignUpViewModel by viewModels()
    private var PIK = ""
    val mapAuth = HashMap<String, String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initObserver()
        PIK = intent.extras?.get(Constants.PARAM_PIK)?.toString() ?: ""

        mapAuth["cu-x-server"] = "8jfy572hf74xfhhg23C343u5u2jfw3240"
        mapAuth["Content-Type"] = "application/json"
        mapAuth["Authorization"] = "Bearer $PIK"
    }

    override val bindingInflater: (LayoutInflater) -> ActivityRegisterUserNameBinding
        get() = ActivityRegisterUserNameBinding::inflate

    override fun initListener() {
        binding.btnRegister.setOnClickListener {
            val fullName = "${binding.etFirstName.text} ${binding.etLastName.text}"
            val countryCode = "+371 "//binding.etCountryCode.text.toString()
            val mobile = binding.etMobile.text.toString()
            val params = RequestRegisterNameAndPhone(
                fullName, countryCode, mobile
            )
//            viewModel.registerUserNameAndPhone(params, mapAuth)
            val intent = VerifyMobileOTPActivity.getIntent(this@RegisterUserNameActivity)
            intent.putExtra(Constants.PARAM_MOBILE, "$countryCode $mobile")
            intent.putExtra(Constants.PARAM_PIK, PIK)
            launchActivity(intent)

        }
    }

    private fun validateAndCallSignUp() {
        //signUpViewModel.validateEmailPassword(
//            binding.etEmail.text.toString(),
//            binding.etPassword.text.toString(), object : ValidEmailPasswordListener {
//                override fun isValid(
//                    isValidEmail: Boolean,
//                    isUppercase: Boolean,
//                    isLowercase: Boolean,
//                    isAnyNumber: Boolean,
//                    isPasswordLengthValid: Boolean
//                ) {
//
//                    val isAgeChecked = binding.cbAge.isChecked
//                    val isAgreement = binding.cbAgreement.isChecked
//                    if (isValidEmail
//                        && isUppercase
//                        && isLowercase
//                        && isAnyNumber
//                        && isPasswordLengthValid
//                        && isAgeChecked
//                        && isAgreement
//                    ) {
//                        mapAuth["cu-x-server"] = "8jfy572hf74xfhhg23C343u5u2jfw3240"
//                        mapAuth["Content-Type"] = "application/json"
//                        mapAuth["Authorization"] = "Bearer $PIK"
//
//                        signUpViewModel.registerEmailPassword(
//                            RequestRegisterEmail(
//                                binding.etEmail.text.toString(),
//                                binding.etPassword.text.toString()
//                            ),
//                            mapAuth
//                        )
//
//                    } else if (!isValidEmail) {
//                        showMessageDialog(
//                            this@RegisterUserNameActivity,
//                            getString(R.string.please_enter_valid_email)
//                        )
//                    } else if (!isUppercase || !isLowercase || !isAnyNumber || !isPasswordLengthValid) {
//                        showMessageDialog(
//                            this@RegisterUserNameActivity,
//                            getString(R.string.please_enter_valid_password)
//                        )
//                    } else if (!isAgeChecked) {
//                        showMessageDialog(
//                            this@RegisterUserNameActivity,
//                            getString(R.string.please_check) + "" + getString(R.string.i_m_at_least_18_years_old_and_agree_to_the_following_terms)
//                        )
//                    } else if (!isAgreement) {
//                        showMessageDialog(
//                            this@RegisterUserNameActivity,
//                            getString(R.string.please_check) + "" + getString(R.string.i_ve_read_and_agree_to_the_e_sign_disclosure_and_consent_to_receive_all_communications_electronically)
//                        )
//                    }
//                }
//            })
    }

    private fun initObserver() {
        viewModel.responseNameAndPhone.observe(this,
            { response ->
                if (response.isSuccessful) {
                    PIK = response.body()?.pik ?: ""
                    val countryCode = "lv"//binding.etCountryCode.text.toString()
                    val mobile = ""//binding.etMobile.text.toString()
                    val intent = VerifyMobileOTPActivity.getIntent(this@RegisterUserNameActivity)
                    intent.putExtra(Constants.PARAM_MOBILE, "$countryCode $mobile")
                    intent.putExtra(Constants.PARAM_PIK, PIK)
                    launchActivity(intent)
                } else {
                    showMessageDialog(this, response?.message() ?: "")
                }
            })
    }
}