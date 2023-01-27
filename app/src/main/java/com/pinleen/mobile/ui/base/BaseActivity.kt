package com.pinleen.mobile.ui.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

    private var _viewBinding: VB? = null
    protected val binding: VB
        get() = _viewBinding!!

    abstract val bindingInflater: (LayoutInflater) -> VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _viewBinding = bindingInflater(layoutInflater)
        setContentView(_viewBinding!!.root)
        initClickListener()
    }

    protected fun showError(errorMessage: String) {
        //if (BuildConfig.DEBUG)
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
    }

    abstract fun initClickListener()
    /**
     * Extension for smarter launching of Activities
     */

    fun launchActivity(context: Context, destination: AppCompatActivity) {
        val intent = Intent(context, destination::class.java)
        startActivity(intent)
    }

    fun showToast(message:String){
        Toast.makeText(this,message,Toast.LENGTH_LONG).show()
    }

}