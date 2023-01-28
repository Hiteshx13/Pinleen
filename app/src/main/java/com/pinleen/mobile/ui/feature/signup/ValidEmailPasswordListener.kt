package com.pinleen.mobile.ui.feature.signup

interface ValidEmailPasswordListener {

    fun isValid(
        isValidEmail: Boolean,
        isUppercase: Boolean,
        isLowercase: Boolean,
        isAnyNumber: Boolean,
        isPasswordLengthValid: Boolean
    )
}