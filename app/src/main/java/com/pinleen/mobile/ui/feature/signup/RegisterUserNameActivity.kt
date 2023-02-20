package com.pinleen.mobile.ui.feature.signup

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
        binding.ccp.detectSIMCountry(true)
        mapAuth["cu-x-server"] = "8jfy572hf74xfhhg23C343u5u2jfw3240"
        mapAuth["Content-Type"] = "application/json"
        mapAuth["Authorization"] = "Bearer $PIK"
    }

    override val bindingInflater: (LayoutInflater) -> ActivityRegisterUserNameBinding
        get() = ActivityRegisterUserNameBinding::inflate

    override fun initListener() {
        binding.btnRegister.setOnClickListener {
            val countryInitials = binding.ccp.selectedCountryNameCode
            val mobile = binding.etMobile.text.toString()
            val params = RequestRegisterNameAndPhone(
                first_name = binding.etFirstName.text.toString(),
                last_name = binding.etLastName.text.toString(),
                country_initials = countryInitials.lowercase(),
                phone_num = mobile
            )
            viewModel.registerUserNameAndPhone(params, mapAuth)
        }
    }

    private fun initObserver() {
        viewModel.responseNameAndPhone.observe(this,
            { response ->
                if (response.isSuccessful) {
                    PIK = response.body()?.pik ?: ""
                    val countryCode = binding.ccp.selectedCountryCode
                    val mobile = binding.etMobile.text.toString()
                    val intent = VerifyMobileOTPActivity.getIntent(this@RegisterUserNameActivity)
                    intent.putExtra(Constants.PARAM_MOBILE, "+$countryCode $mobile")
                    intent.putExtra(Constants.PARAM_PIK, PIK)
                    launchActivity(intent)
                } else {
                    showMessageDialog(this, response?.message() ?: "")
                }
            })
    }
}