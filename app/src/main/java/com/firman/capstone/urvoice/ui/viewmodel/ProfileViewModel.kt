package com.firman.capstone.urvoice.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.firman.capstone.urvoice.data.remote.models.CurrentUserResponse
import com.firman.capstone.urvoice.data.remote.models.DeleteUserResponse
import com.firman.capstone.urvoice.data.remote.models.UserLogoutResponse
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
    private val userRepository: UserRepository
) : ViewModel() {
    private val _currentUserProfile =
        MutableStateFlow<ResultState<CurrentUserResponse>>(ResultState.Initial)
    val currentUserProfile: StateFlow<ResultState<CurrentUserResponse>> =
        _currentUserProfile.asStateFlow()

    private val _deleteUser = MutableStateFlow<ResultState<DeleteUserResponse>>(ResultState.Initial)
    private val _logoutUser = MutableStateFlow<ResultState<UserLogoutResponse>>(ResultState.Initial)

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

    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
            _logoutUser.value = ResultState.Initial
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
