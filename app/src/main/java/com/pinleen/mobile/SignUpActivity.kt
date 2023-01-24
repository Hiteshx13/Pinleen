package com.pinleen.mobile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import com.pinleen.mobile.databinding.ActivitySignupBinding
import com.pinleen.mobile.network.retrofit.PinleenAPI
import com.pinleen.mobile.network.retrofit.RetrofitHelper
import com.pinleen.mobile.ui.base.BaseActivity
import com.pinleen.mobile.utils.Constents
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class SignUpActivity : BaseActivity<ActivitySignupBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val quotesApi = RetrofitHelper.getInstance().create(PinleenAPI::class.java)
        // launching a new coroutine
        GlobalScope.launch {
            //val result = PinleenAPI.getUser()
//            if (result != null)
//            // Checking the results
//                Log.d("h: ", result.body().toString())
        }


        binding.btnRegister.setOnClickListener {
            val email = binding.etEmail.text
            val intent = Intent(this, SignUpVerificationActivity::class.java)
            intent.putExtra(Constents.PARAM_EMAIL, email)
            startActivity(intent)
        }
    }

    override val bindingInflater: (LayoutInflater) -> ActivitySignupBinding
        get() = ActivitySignupBinding::inflate
}