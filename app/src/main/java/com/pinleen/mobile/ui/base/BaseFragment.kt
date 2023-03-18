package com.pinleen.mobile.ui.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<VB : ViewBinding> : Fragment() {

    private var _bi: VB? = null
    protected val binding: VB get() = _bi!!
    lateinit var mActivity: AppCompatActivity
    abstract val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _bi = bindingInflater(inflater, container, false)
        mActivity = requireActivity() as AppCompatActivity
        return _bi!!.root
    }

    protected fun showError(errorMessage: String) {
        //if (BuildConfig.DEBUG)
        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _bi = null
    }

    fun showToast( message: String) {
       Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

}