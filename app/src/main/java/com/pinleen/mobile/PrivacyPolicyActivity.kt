package com.pinleen.mobile

import android.content.Context
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
import com.pinleen.mobile.databinding.ActivityPrivacyPolicyBinding
import com.pinleen.mobile.ui.base.BaseActivity
import com.pinleen.mobile.ui.feature.login.LoginActivity
import com.pinleen.mobile.utils.Constants
import com.pinleen.mobile.utils.PreferenceHelper


class PrivacyPolicyActivity : BaseActivity<ActivityPrivacyPolicyBinding>() {
    companion object {
        fun getIntent(mContext: Context): Intent {
            return Intent(mContext, PrivacyPolicyActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val txtWelcome =
            resources.getString(R.string.welcome_to_) + " " + resources.getString(R.string.app_name)
        binding.tvWelcome.text = txtWelcome
        val ss = SpannableString(resources.getString(R.string.read_privacy))
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                val openURL = Intent(android.content.Intent.ACTION_VIEW)
                openURL.data = Uri.parse(resources.getString(R.string.url_privacy))
                startActivity(openURL)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = true
            }
        }
        ss.setSpan(clickableSpan, ss.length - 12, ss.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.tvReadPrivacy.text = ss
        binding.tvReadPrivacy.movementMethod = LinkMovementMethod.getInstance()
    }

    override val bindingInflater: (LayoutInflater) -> ActivityPrivacyPolicyBinding
        get() = ActivityPrivacyPolicyBinding::inflate

    override fun initListener() {
        binding.btnAgreeAndContinue.setOnClickListener {
            PreferenceHelper.setBoolean(this, Constants.PREF_IS_PRIVACY_POLICY_SHOWN, true)
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}