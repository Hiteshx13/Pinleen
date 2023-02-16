package com.pinleen.mobile.data.models.request

data class RequestRegisterNameAndPhone(
    val first_name: String,
    val last_name: String,
    val country_initials: String,
    val phone_num: String
)
