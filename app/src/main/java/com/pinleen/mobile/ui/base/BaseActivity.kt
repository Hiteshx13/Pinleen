package com.pinleen.mobile.ui.base

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import org.jivesoftware.smack.android.AndroidSmackInitializer

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

    private var _viewBinding: VB? = null
    protected val binding: VB
        get() = _viewBinding!!

    abstract val bindingInflater: (LayoutInflater) -> VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _viewBinding = bindingInflater(layoutInflater)
        setContentView(_viewBinding!!.root)
        initListener()
        AndroidSmackInitializer.initialize(this);

    }



    abstract fun initListener()

    /**
     * Extension for smarter launching of Activities
     */

    fun launchActivity(intent: Intent) {
        startActivity(intent)
    }

    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }



}