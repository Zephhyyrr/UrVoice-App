package com.firman.capstone.urvoice.data.remote.service

import com.firman.capstone.urvoice.utils.ApiConstant
import retrofit2.http.GET

interface ApiService {
    @GET(ApiConstant.USER_REGISTER)
    fun registerUser(): String
}