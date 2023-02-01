package com.pinleen.mobile.utils

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.pinleen.mobile.R
import com.pinleen.mobile.databinding.DialogImageSelectionBinding
import com.pinleen.mobile.databinding.DialogMessageBinding
import com.pinleen.mobile.databinding.DialogRationalMessageBinding


fun showImageSelectionDialog(
    context: Context,
    isCancelable: Boolean,
    listener: ItemClickCameraDialog
): Dialog {

    val inflater = LayoutInflater.from(context)
    val binding: DialogImageSelectionBinding =
        DataBindingUtil.inflate(inflater, R.layout.dialog_image_selection, null, false)


    val mDialog = Dialog(context)
    mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    mDialog.setContentView(binding.root)
    mDialog.setCanceledOnTouchOutside(isCancelable)
    mDialog.window?.setBackgroundDrawableResource(R.color.blackTrans90)
    mDialog.window?.setLayout(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT
    )
    mDialog.window?.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
    binding.btnCancel.setOnClickListener {
        mDialog.dismiss()
    }
    binding.btnChooseFromGallery.setOnClickListener {
        listener.onClickGallery()
        mDialog.dismiss()
    }
    binding.btnTakePhoto.setOnClickListener {
        listener.onClickCamera()
        mDialog.dismiss()
    }

    mDialog.show()
    return mDialog
}

fun showRationalMessageDialog(
    context: Context,
    message: String,
    listener: ItemClickListener
): Dialog {

    val inflater = LayoutInflater.from(context)
    val binding: DialogRationalMessageBinding =
        DataBindingUtil.inflate(inflater, R.layout.dialog_rational_message, null, false)
    val mDialog = Dialog(context)
    mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    mDialog.setContentView(binding.root)
    mDialog.setCanceledOnTouchOutside(false)
    mDialog.window?.setBackgroundDrawableResource(R.drawable.bg_button_gray_rounded)
    binding.txtMessage.text = message

    binding.btnOpenSettings.setOnClickListener {
        listener.onClick()
        mDialog.dismiss()
    }
    binding.btnCancel.setOnClickListener {
        mDialog.dismiss()
    }

    mDialog.show()
    return mDialog
}

fun showMessageDialog(
    context: Context,
    message: String

): Dialog {

    val inflater = LayoutInflater.from(context)
    val binding: DialogMessageBinding =
        DataBindingUtil.inflate(inflater, R.layout.dialog_message, null, false)
    val mDialog = Dialog(context)

    mDialog.window?.setBackgroundDrawableResource(R.color.transparent)
    mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    mDialog.setContentView(binding.root)
    mDialog.setCanceledOnTouchOutside(false)
    mDialog.window?.setLayout(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )
    binding.txtMessage.text = message

    binding.btnCancel.setOnClickListener {
        mDialog.dismiss()
    }

    mDialog.show()
    return mDialog
}


interface ItemClickCameraDialog {
    fun onClickGallery()
    fun onClickCamera()
}

interface ItemClickListener {
    fun onClick()
}
