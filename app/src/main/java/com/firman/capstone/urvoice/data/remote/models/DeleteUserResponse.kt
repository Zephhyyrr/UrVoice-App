package com.firman.capstone.urvoice.data.remote.models

import com.google.gson.annotations.SerializedName

data class DeleteUserResponse(
    @SerializedName("message")
    val message: String?,
    @SerializedName("success")
    val success: Boolean?
)