package com.pinleen.mobile.ui.feature.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.widget.AppCompatImageView
import com.pinleen.mobile.databinding.ActivityDashboardBinding
import com.pinleen.mobile.ui.base.BaseActivity


class DashboardActivity : BaseActivity<ActivityDashboardBinding>() {

    companion object {
        fun getIntent(mContext: Context): Intent {
            return Intent(mContext, DashboardActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initObserver()
        changeStatue(binding.icHomeShop)
    }

    override val bindingInflater: (LayoutInflater) -> ActivityDashboardBinding
        get() = ActivityDashboardBinding::inflate

    override fun initListener() {

        binding.icHomeChat.setOnClickListener {
            changeStatue(binding.icHomeChat)
        }
        binding.icHomeContacts.setOnClickListener {
            changeStatue(binding.icHomeContacts)
        }
        binding.icHomeShop.setOnClickListener {
            changeStatue(binding.icHomeShop)
        }
        binding.icHomeMe.setOnClickListener {
            changeStatue(binding.icHomeMe)
        }
    }

    private fun changeStatue(view: AppCompatImageView) {
        binding.icHomeShop.isSelected = false
        binding.icHomeMe.isSelected = false
        binding.icHomeContacts.isSelected = false
        binding.icHomeChat.isSelected = false
        view.isSelected = true

    }

    private fun initObserver() {

    }
}