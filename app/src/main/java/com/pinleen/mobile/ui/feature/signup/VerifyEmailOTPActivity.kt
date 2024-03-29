package com.pinleen.mobile.ui.feature.signup

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.pinleen.mobile.R
import com.pinleen.mobile.data.models.request.RequestResendEmailOTP
import com.pinleen.mobile.data.models.request.RequestVerifyEmailOTP
import com.pinleen.mobile.databinding.ActivityEmailOtpVerificationBinding
import com.pinleen.mobile.ui.base.BaseActivity
import com.pinleen.mobile.utils.Constants.PARAM_EMAIL
import com.pinleen.mobile.utils.Constants.PARAM_PIK
import com.pinleen.mobile.utils.TextWatcher
import com.pinleen.mobile.utils.showMessageDialog
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.ticker


class VerifyEmailOTPActivity : BaseActivity<ActivityEmailOtpVerificationBinding>() {

    companion object {
        fun getIntent(mContext: Context): Intent {
            return Intent(mContext, VerifyEmailOTPActivity::class.java)
        }
    }

    private val viewModel: SignUpVerificationViewModel by viewModels()
    private val mapAuth = HashMap<String, String>()
    private var PIK = ""
    private var EMAIL=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        PIK = intent.extras?.get(PARAM_PIK)?.toString() ?: ""
        EMAIL = intent.extras?.get(PARAM_EMAIL)?.toString().toString()
        binding.tvEmail.text = EMAIL

        initObserver()
        setFocus()

        lifecycleScope.launchWhenCreated {
            tickerFlow()
        }

        mapAuth["cu-x-server"] = "8jfy572hf74xfhhg23C343u5u2jfw3240"
        mapAuth["Content-Type"] = "application/json"
        mapAuth["Authorization"] = "Bearer $PIK"
    }

    override fun initListener() {
        binding.btnConfirm.setOnClickListener {
            validateAndCallVerifyOTP()
        }

        binding.ivback.setOnClickListener {
            finish()
        }
        binding.tvResend.setOnClickListener {
            binding.llResend.visibility = View.GONE
            binding.llCounter.visibility = View.VISIBLE
            viewModel.callResendEmailOTP(mapAuth)
        }
    }

    private fun initObserver() {
        viewModel.responseVerifyEmailOTP.observe(this,
            { response ->
                Log.d("RESPONSE","$response")
                if (response.isSuccessful) {
                    PIK=response.body()?.pik?:""
                    val intent=RegisterUserNameActivity.getIntent(this)
                    intent.putExtra(PARAM_PIK,PIK)
                    launchActivity(intent)
                } else {
                    showMessageDialog(this, response?.message() ?: "")
                }
            })

        viewModel.responseResendEmailOTP.observe(this,
            { response ->
                PIK=response.body()?.pik?:""
                showMessageDialog(this, response?.message() ?: "")
            })
    }

    private fun setFocus() {
        TextWatcher.setWatcher(binding.edtPasscode1, binding.edtPasscode2)
        TextWatcher.setWatcher(binding.edtPasscode2, binding.edtPasscode3)
        TextWatcher.setWatcher(binding.edtPasscode3, binding.edtPasscode4)
    }


    @ObsoleteCoroutinesApi
    private suspend fun tickerFlow() {
        val ticker = ticker(1000, 0)
        var count = 61
        for (event in ticker) {
            count--
            val fCount=String.format("00:%02d", count)
            binding.tvTimer.text = fCount
            if (count == 0) {
                binding.llCounter.visibility = View.GONE
                binding.llResend.visibility = View.VISIBLE
                tickerFlow()
                break
            }
        }
        ticker.cancel()
    }


    private fun validateAndCallVerifyOTP() {
        val passOne = binding.edtPasscode1.text
        val passTwo = binding.edtPasscode2.text
        val passThree = binding.edtPasscode3.text
        val passFour = binding.edtPasscode4.text

        val otp = "$passOne$passTwo$passThree$passFour"
        if (viewModel.validateOTP(otp)) {

            viewModel.verifyEmailOTP(RequestVerifyEmailOTP(otp), mapAuth)
        } else {
            showMessageDialog(this, getString(R.string.please_enter_valid_otp))
        }
    }

    override val bindingInflater: (LayoutInflater) -> ActivityEmailOtpVerificationBinding
        get() = ActivityEmailOtpVerificationBinding::inflate
}