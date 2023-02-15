package com.pinleen.mobile.data.models.request

data class RequestRegisterNameAndPhone(
    val name_first: String,
    val name_last: String,
    val country_initials: String,
    val phone_num: String
)
