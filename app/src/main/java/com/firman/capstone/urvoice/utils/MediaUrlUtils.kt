package com.firman.capstone.urvoice.utils

import com.firman.capstone.urvoice.BuildConfig

object MediaUrlUtils {
    fun buildImageUrl(imageUrl: String?): String {
        return when {
            imageUrl.isNullOrBlank() -> ""
            imageUrl.startsWith("http://") || imageUrl.startsWith("https://") -> imageUrl
            imageUrl.startsWith("/uploads/") -> "${BuildConfig.BASE_URL.trimEnd('/')}$imageUrl"
            else -> "${BuildConfig.BASE_URL.trimEnd('/')}/uploads/$imageUrl"
        }
    }
}