package com.pinleen.mobile.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings

object AppUtils {

    fun openAppSettings(mContext: Context){
        val intent =
            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", mContext.packageName, null)
        intent.data = uri
        mContext.startActivity(intent)
    }
}