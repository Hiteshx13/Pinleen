package com.pinleen.mobile.data.models.response


import androidx.annotation.Keep

@Keep
data class ResponseLogin(
    val message: String,
    val status: String,
    val user_data: UserData
) {
    @Keep
    data class UserData(
        val country: String,
        val country_code: String,
        val domain_part_of_jid: String,
        val email: String,
        val user_xmpp_pass: String,
        val username_part_of_jid: String
    )
}