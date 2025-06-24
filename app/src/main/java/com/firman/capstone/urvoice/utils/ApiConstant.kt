package com.firman.capstone.urvoice.utils

import com.firman.capstone.urvoice.BuildConfig

object ApiConstant {
    const val BASE_URL = BuildConfig.BASE_URL

    // User Hit API
    const val USER_REGISTER = "api/users/register"
    const val USER_LOGIN = "api/users/login"
    const val USER_CURRENT = "/api/users/current"
    const val USER_LOGOUT = "api/users/logout"
    const val USER_PROFILE = "api/users/uploadPhotos"
    const val USER_UPDATE = "api/users/update"
    const val USER_DELETE = "api/users/delete"

    // Article Hit API
    const val ARTICLE_LIST = "api/articles/getAll"
    const val ARTICLE_DETAIL = "api/articles/{id}"

    // Model Hit API
    const val SPEECH_TO_TEXT = "api/models/speech-to-text"
    const val ANALYZE_TEXT = "api/models/analyze-speech"

}