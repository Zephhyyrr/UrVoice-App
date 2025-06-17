package com.firman.capstone.urvoice.utils

import com.firman.capstone.urvoice.BuildConfig

object ImageUrlUtils {
    fun buildImageUrl(imageUrl: String?): String {
        return when {
            imageUrl.isNullOrBlank() -> ""
            imageUrl.startsWith("http://") || imageUrl.startsWith("https://") -> imageUrl
            else -> "${BuildConfig.BASE_URL.trimEnd('/')}/uploads/$imageUrl"
        }
    }
}