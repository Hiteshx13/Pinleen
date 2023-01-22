package com.hr.pinleen

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import androidx.lifecycle.lifecycleScope
import com.hr.pinleen.databinding.ActivitySignupVerificationCodeBinding
import com.hr.pinleen.ui.base.BaseActivity
import com.hr.pinleen.utils.Constents.PARAM_EMAIL
import com.hr.pinleen.utils.TextWatcher
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import java.time.Duration
import java.time.LocalDateTime


class SignUpVerificationActivity : BaseActivity<ActivitySignupVerificationCodeBinding>() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val email = intent.getStringExtra(PARAM_EMAIL)
        binding.tvEmail.text=email
        setFocus()

        lifecycleScope.launchWhenCreated {
            tickerFlow()
        }

        binding.btnConfirm.setOnClickListener {
//            val email = binding.etEmail.text
//            val intent = Intent(this, ActivitySignupVerificationCodeBinding::class.java)
//            intent.putExtra("", email)
//            startActivity(intent)
//            finish()
        }
    }

    private fun setFocus() {
       TextWatcher.setWatcher( binding.edtPasscode1, binding.edtPasscode2)
       TextWatcher.setWatcher( binding.edtPasscode2, binding.edtPasscode3)
       TextWatcher.setWatcher( binding.edtPasscode3, binding.edtPasscode4)
    }


    @ObsoleteCoroutinesApi
    private suspend fun  tickerFlow(){
        val tickerChannel = ticker(delayMillis = 1_000, initialDelayMillis = 0)

        val ticker = ticker(1000, 0)

        var count = 61

        for (event in ticker) {
            count--
            binding.tvTimer.text="00:$count"
            if (count == 0) {
                break
            }
        }

        ticker.cancel()
    }

    override val bindingInflater: (LayoutInflater) -> ActivitySignupVerificationCodeBinding
        get() = ActivitySignupVerificationCodeBinding::inflate
}