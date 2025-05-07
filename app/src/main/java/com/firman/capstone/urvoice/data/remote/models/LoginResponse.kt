package com.firman.capstone.urvoice.data.remote.models

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("message")
    val message: String?,
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("data")
    val data: LoginData?,

    ) {
    data class LoginData(
        @SerializedName("email")
        val email: String?,
        @SerializedName("accessToken")
        val accessToken: String?,
        @SerializedName("refreshToken")
        val refreshToken: String?,
    )
}

