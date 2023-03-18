package com.pinleen.mobile.utils

import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.widget.AppCompatEditText

object TextWatcher {

    fun setWatcher( etCurrent:AppCompatEditText,etNext:AppCompatEditText){

    val watcher= object: TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun afterTextChanged(s: Editable?) {
            if (s.toString().isEmpty()) {
                etCurrent.requestFocus()
            } else {
                etNext.requestFocus()

            }
        }
    }

        etCurrent.addTextChangedListener(watcher)
}}