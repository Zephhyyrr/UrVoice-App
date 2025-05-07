package com.firman.capstone.urvoice.data.remote.service

import com.firman.capstone.urvoice.data.remote.models.LoginResponse
import com.firman.capstone.urvoice.utils.ApiConstant
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface LoginService {

    @FormUrlEncoded
    @POST(ApiConstant.USER_LOGIN)
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse
}