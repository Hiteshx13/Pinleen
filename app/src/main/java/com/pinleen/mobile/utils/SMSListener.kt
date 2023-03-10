package com.pinleen.mobile.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.telephony.SmsMessage


class SMSListener : BroadcastReceiver() {


    companion object {
        interface Common {
            interface OTPListener {
                fun onOTPReceived(otp: String)
            }
        }

        var mListener // this listener will do the magic of throwing the extracted OTP to all the bound views.
                : Common.OTPListener? = null

        fun bindListener(listener: Common.OTPListener) {
            mListener = listener
        }

        fun unbindListener() {
            mListener = null
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        val data: Bundle? = intent?.extras

        var pdus: Array<Any?>? = arrayOfNulls(0)
        if (data != null) {
            pdus =
                arrayOf(data["pdus"]) // the pdus key will contain the newly received SMS
        }

        if (pdus != null) {
            for (pdu in pdus) { // loop through and pick up the SMS of interest
                val smsMessage: SmsMessage = SmsMessage.createFromPdu(pdu as ByteArray?)

                // your custom logic to filter and extract the OTP from relevant SMS - with regex or any other way.
                mListener?.onOTPReceived("Extracted OTP")
                break
            }
        }
    }
}