package com.hr.pinleen

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import com.hr.pinleen.databinding.ActivitySignupVerificationSuccessBinding
import com.hr.pinleen.ui.base.BaseActivity


class SignUpVerificationSuccessActivity : BaseActivity<ActivitySignupVerificationSuccessBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.btnGetStarted.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override val bindingInflater: (LayoutInflater) -> ActivitySignupVerificationSuccessBinding
        get() = ActivitySignupVerificationSuccessBinding::inflate
}