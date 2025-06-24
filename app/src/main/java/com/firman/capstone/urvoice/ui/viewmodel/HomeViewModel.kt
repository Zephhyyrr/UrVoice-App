package com.firman.capstone.urvoice.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.firman.capstone.urvoice.data.remote.models.ArticleResponse
import com.firman.capstone.urvoice.data.remote.models.CurrentUserResponse
import com.firman.capstone.urvoice.data.repository.article.ArticleRepository
import com.firman.capstone.urvoice.data.repository.user.UserRepository
import com.firman.capstone.urvoice.utils.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val articleRepository: ArticleRepository
) : ViewModel() {

    private val _userState = MutableStateFlow<ResultState<CurrentUserResponse>>(ResultState.Initial)
    val userState: StateFlow<ResultState<CurrentUserResponse>> = _userState.asStateFlow()

    private val _articlesState =
        MutableStateFlow<ResultState<List<ArticleResponse.Data>>>(ResultState.Initial)
    val articlesState: StateFlow<ResultState<List<ArticleResponse.Data>>> =
        _articlesState.asStateFlow()


    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        getCurrentUser()
        getAllArticles()
    }

    fun getCurrentUser() {
        viewModelScope.launch {
            _userState.value = ResultState.Loading
            try {
                userRepository.getCurrentUser().collect { result ->
                    _userState.value = result
                }
            } catch (e: Exception) {
                _userState.value = ResultState.Error("Failed to load user data: ${e.message}")
            }
        }
    }

    fun getAllArticles() {
        viewModelScope.launch {
            _articlesState.value = ResultState.Loading
            articleRepository.getAllArticle().collect { result ->
                _articlesState.value = result
            }
        }
    }
}