package com.ahmetkarli.haberim.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkarli.haberim.models.Article
import com.ahmetkarli.haberim.models.ArticleDbModel
import com.ahmetkarli.haberim.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsDetailViewModel
@Inject
constructor(private val repository: NewsRepository) : ViewModel(){

    fun saveArticle(article: ArticleDbModel) = viewModelScope.launch {
        repository.upsert(article)
    }

}