package com.firman.capstone.urvoice.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.firman.capstone.urvoice.data.remote.models.ArticleResponse
import com.firman.capstone.urvoice.data.repository.article.ArticleRepository
import com.firman.capstone.urvoice.utils.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor(
    private val articleRepository: ArticleRepository
) : ViewModel() {

    private val _articles =
        MutableStateFlow<ResultState<List<ArticleResponse.Data>>>(ResultState.Initial)
    val articles: StateFlow<ResultState<List<ArticleResponse.Data>>> = _articles

    init {
        getAllArticles()
    }

    fun getAllArticles() {
        _articles.value = ResultState.Loading
        viewModelScope.launch {
            articleRepository.getAllArticle().collect { result ->
                when (result) {
                    is ResultState.Success -> {
                        _articles.value = ResultState.Success(
                            data = result.data,
                            successMessage = result.successMessage
                        )
                    }

                    is ResultState.Error -> {
                        _articles.value = ResultState.Error(result.errorMessage)
                    }

                    is ResultState.Loading -> {
                        _articles.value = ResultState.Loading
                    }

                    is ResultState.Initial -> {
                        _articles.value = ResultState.Initial
                    }
                }
            }
        }
    }

    fun getArticleById(id: Int) {
        _articles.value = ResultState.Loading
        viewModelScope.launch {
            articleRepository.getArticleById(id).collect { result ->
                when (result) {
                    is ResultState.Success -> {
                        _articles.value = ResultState.Success(
                            data = result.data,
                            successMessage = result.successMessage
                        )
                    }

                    is ResultState.Error -> {
                        _articles.value = ResultState.Error(result.errorMessage)
                    }

                    is ResultState.Loading -> {
                        _articles.value = ResultState.Loading
                    }

                    is ResultState.Initial -> {
                        _articles.value = ResultState.Initial
                    }
                }
            }
        }
    }
}
