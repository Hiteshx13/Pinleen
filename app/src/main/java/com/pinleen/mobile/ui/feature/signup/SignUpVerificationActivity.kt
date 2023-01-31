package com.pinleen.mobile.ui.feature.signup

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.lifecycle.lifecycleScope
import com.pinleen.mobile.databinding.ActivitySignupVerificationCodeBinding
import com.pinleen.mobile.ui.base.BaseActivity
import com.pinleen.mobile.utils.Constants.PARAM_EMAIL
import com.pinleen.mobile.utils.TextWatcher
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.ticker


class SignUpVerificationActivity : BaseActivity<ActivitySignupVerificationCodeBinding>() {


    companion object {
        fun getIntent(mContext: Context): Intent {
            return Intent(mContext, SignUpVerificationActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val email = intent.extras?.get(PARAM_EMAIL)?.toString()
        binding.tvEmail.text = email

        initListener()
        setFocus()

        lifecycleScope.launchWhenCreated {
            tickerFlow()
        }
    }

    override fun initListener() {
        binding.btnConfirm.setOnClickListener {
            val intent = Intent(this, SignUpVerificationSuccessActivity::class.java)
            startActivity(intent)
        }

        binding.ivback.setOnClickListener {
            finish()
        }
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
            binding.tvTimer.text = "00:$count"
            if (count == 0) {
                break
            }
        }
        ticker.cancel()
    }

    override val bindingInflater: (LayoutInflater) -> ActivitySignupVerificationCodeBinding
        get() = ActivitySignupVerificationCodeBinding::inflate
}