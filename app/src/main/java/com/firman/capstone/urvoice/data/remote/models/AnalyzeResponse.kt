package com.firman.capstone.urvoice.data.remote.models

import com.google.gson.annotations.SerializedName

data class AnalyzeResponse(
    @SerializedName("message")
    val message: String? = null,

    @SerializedName("success")
    val success: Boolean = false,

    @SerializedName("data")
    val data: List<Data>? = null
) {
    data class Data(
        @SerializedName("id")
        val id: Int? = null
    )
}