package com.pinleen.mobile.ui.feature.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import com.google.gson.Gson
import com.pinleen.mobile.R
import com.pinleen.mobile.databinding.ActivityLoginBinding
import com.pinleen.mobile.ui.base.BaseActivity
import com.pinleen.mobile.ui.feature.dashboard.DashboardActivity
import com.pinleen.mobile.ui.feature.signup.RegisterEmailActivity
import com.pinleen.mobile.ui.feature.signup.ValidEmailPasswordListener
import com.pinleen.mobile.utils.Constants.PREF_IS_LOGGED_IN
import com.pinleen.mobile.utils.Constants.PREF_USER_LOGIN_DATA
import com.pinleen.mobile.utils.PreferenceHelper
import com.pinleen.mobile.utils.showMessageDialog


class LoginActivity : BaseActivity<ActivityLoginBinding>() {

    companion object {
        fun getIntent(mContext: Context): Intent {
            return Intent(mContext, LoginActivity::class.java)
        }
    }

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
            validateAndLogin()
        }
        binding.tvForgotPassword.setOnClickListener {
            launchActivity(ForgotPasswordActivity.getIntent(this))
        }
        binding.tvSignUp.setOnClickListener {
            launchActivity(RegisterEmailActivity.getIntent(this))
        }
    }


    private fun validateAndLogin() {
        viewModel.validateAndLogin(
            binding.etEmail.text.toString(),
            binding.etPassword.text.toString(), object : ValidEmailPasswordListener {
                override fun isValid(
                    isValidEmail: Boolean,
                    isUppercase: Boolean,
                    isLowercase: Boolean,
                    isAnyNumber: Boolean,
                    isPasswordLengthValid: Boolean
                ) {
                    if (!isValidEmail) {
                        showMessageDialog(
                            this@LoginActivity,
                            getString(R.string.please_enter_valid_email)
                        )
                    } else if (!isUppercase || !isLowercase || !isAnyNumber || !isPasswordLengthValid) {
                        showMessageDialog(
                            this@LoginActivity,
                            getString(R.string.please_enter_valid_password)
                        )
                    }
                }
            })
    }

    private fun initObserver() {
        viewModel.responseLogin.observe(this, { response ->

            if (response.isSuccessful) {
                PreferenceHelper.setBoolean(this, PREF_IS_LOGGED_IN, true)
                PreferenceHelper.setString(this,PREF_USER_LOGIN_DATA, Gson().toJson(response.body()?.user_data))

                launchActivity(DashboardActivity.getIntent(this))
            } else {
                showMessageDialog(this, response.message())
            }
        }
        )
    }
}