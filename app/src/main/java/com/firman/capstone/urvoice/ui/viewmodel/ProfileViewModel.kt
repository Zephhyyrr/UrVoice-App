package com.firman.capstone.urvoice.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.firman.capstone.urvoice.data.local.datastore.AuthPreferences
import com.firman.capstone.urvoice.data.remote.models.CurrentUserResponse
import com.firman.capstone.urvoice.data.remote.models.DeleteUserResponse
import com.firman.capstone.urvoice.data.remote.models.UserLogoutResponse
import com.firman.capstone.urvoice.data.remote.models.UserResponse
import com.firman.capstone.urvoice.data.repository.user.UserRepository
import com.firman.capstone.urvoice.utils.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authPreferences: AuthPreferences
) : ViewModel() {
    private val _currentUserProfile =
        MutableStateFlow<ResultState<CurrentUserResponse>>(ResultState.Initial)
    val currentUserProfile: StateFlow<ResultState<CurrentUserResponse>> =
        _currentUserProfile.asStateFlow()

    private val _updateUserState = MutableStateFlow<ResultState<UserResponse>>(ResultState.Initial)
    val updateUserState: StateFlow<ResultState<UserResponse>> = _updateUserState

    private val _deleteUser = MutableStateFlow<ResultState<DeleteUserResponse>>(ResultState.Initial)
    private val _logoutUser = MutableStateFlow<ResultState<UserLogoutResponse>>(ResultState.Initial)
    val logoutUserState: StateFlow<ResultState<UserLogoutResponse>> = _logoutUser.asStateFlow()


    init {
        getCurrentUser()
    }

    fun getCurrentUser() {
        viewModelScope.launch {
            _currentUserProfile.value = ResultState.Loading
            try {
                userRepository.getCurrentUser().collect { result ->
                    _currentUserProfile.value = result
                }
            } catch (e: Exception) {
                _currentUserProfile.value =
                    ResultState.Error("Failed to load user data: ${e.message}")
            }
        }
    }

    fun updateUserProfile(name: String, password: String?) {
        viewModelScope.launch {
            userRepository.updateUser(name, password ?: "")
                .collect { result ->
                    _updateUserState.value = result
                    if (result is ResultState.Success) {
                        getCurrentUser()
                    }
                }
        }
    }

    fun logout() {
        viewModelScope.launch {
            _logoutUser.value = ResultState.Loading
            try {
                userRepository.logout().collect { result ->
                    _logoutUser.value = result
                    if (result is ResultState.Success) {
                        authPreferences.clearSession()
                    }
                }
            } catch (e: Exception) {
                _logoutUser.value = ResultState.Error("Logout failed: ${e.message}")
            }
        }
    }

    fun deleteUser() {
        viewModelScope.launch {
            _deleteUser.value = ResultState.Loading
            try {
                userRepository.deleteUser().collect { result ->
                    _deleteUser.value = result
                }
            } catch (e: Exception) {
                _deleteUser.value = ResultState.Error("Failed to delete use: ${e.message}")
            }
        }
    }
}
