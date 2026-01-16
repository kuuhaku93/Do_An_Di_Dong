package com.example.libraryoffreelancer.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.json.Json

@Serializable
data class APIResponse(
    val success: Boolean,
    val message: String
)
@Serializable
data class Account(
    val username: String,
    val password: String,
    val email: String,
    val full_name: String,
    val phone_number: String
)
@Serializable
data class LoginResult(
    @SerialName("success")
    var isSuccess: Boolean = false,
    @SerialName("message")
    var message: String = "",
    var account_id:Int=0,
    var token: String? = "",
    var employer_status: Boolean? = false,
    var freelancer_status: Boolean? = false,
)
data class Check(
    var isSuccess: Boolean,
    var message: String,
)

val customJson= Json {
    ignoreUnknownKeys = true
    coerceInputValues = true
}