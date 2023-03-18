package com.pinleen.mobile.ui.feature.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.pinleen.mobile.R
import com.pinleen.mobile.data.models.request.RequestVerifyMobileOTP
import com.pinleen.mobile.databinding.ActivityMobileOtpVerificationBinding
import com.pinleen.mobile.databinding.ActivityVerifyVerificationOtpBinding
import com.pinleen.mobile.ui.base.BaseActivity
import com.pinleen.mobile.ui.feature.signup.SignUpVerificationSuccessActivity
import com.pinleen.mobile.ui.feature.signup.SignUpVerificationViewModel
import com.pinleen.mobile.utils.Constants.PARAM_MOBILE
import com.pinleen.mobile.utils.Constants.PARAM_PIK
import com.pinleen.mobile.utils.TextWatcher
import com.pinleen.mobile.utils.showMessageDialog
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.ticker


class VerifyVerificationOTPActivity : BaseActivity<ActivityVerifyVerificationOtpBinding>() {

    companion object {
        fun getIntent(mContext: Context): Intent {
            return Intent(mContext, VerifyVerificationOTPActivity::class.java)
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //binding.tvMobile.text = intent.extras?.get(PARAM_MOBILE)?.toString()

        initObserver()
        setFocus()

        lifecycleScope.launchWhenCreated {
            tickerFlow()
        }

    }

    override fun initListener() {
        binding.btnConfirm.setOnClickListener {
            launchActivity(SetNewPasswordActivity.getIntent(this))
        }

        binding.ivback.setOnClickListener {
            finish()
        }
        binding.btnResend.setOnClickListener {
            binding.btnResend.isClickable=false
            binding.btnResend.alpha=0.6F
            lifecycleScope.launchWhenCreated {
                tickerFlow()
            }

        }
    }

    private fun initObserver() {

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
            val fCount = String.format("00:%02d", count)
            binding.tvTimer.text = fCount
            if (count == 0) {
//                binding.llCounter.visibility = View.GONE
                binding.btnResend.isClickable=true
                binding.btnResend.alpha=1.0F
//                binding.llResend.visibility = View.VISIBLE
                tickerFlow()
                break
            }
        }
        ticker.cancel()
    }




    override val bindingInflater: (LayoutInflater) -> ActivityVerifyVerificationOtpBinding
        get() = ActivityVerifyVerificationOtpBinding::inflate
}