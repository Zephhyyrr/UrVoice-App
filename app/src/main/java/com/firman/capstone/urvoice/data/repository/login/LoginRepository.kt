package com.firman.capstone.urvoice.data.repository.login

import com.firman.capstone.urvoice.data.remote.models.LoginResponse
import com.firman.capstone.urvoice.utils.ResultState

interface LoginRepository {
    suspend fun login(email: String, password: String): ResultState<LoginResponse>
}
