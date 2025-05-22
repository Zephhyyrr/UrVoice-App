package com.firman.capstone.urvoice.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.firman.capstone.urvoice.data.local.datastore.AuthPreferences
import com.firman.capstone.urvoice.data.local.datastore.OnboardingPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    authPreferences: AuthPreferences,
    onboardingPreferences: OnboardingPreferences
) : ViewModel() {

    data class AppStartupState(
        val isOnboardingCompleted: Boolean = false,
        val isLoggedIn: Boolean = false
    )

    val appStartupState: StateFlow<AppStartupState> = combine(
        onboardingPreferences.hasSeenOnboarding,
        authPreferences.authToken
    ) { hasSeenOnboarding, token ->
        AppStartupState(
            isOnboardingCompleted = hasSeenOnboarding,
            isLoggedIn = !token.isNullOrEmpty()
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AppStartupState()
    )
}