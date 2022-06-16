package com.ahmetkarli.haberim.ui.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkarli.haberim.models.ArticleDbModel
import com.ahmetkarli.haberim.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel
@Inject
constructor(private val repository: NewsRepository) : ViewModel() {

    val isLoading= MutableLiveData<Boolean>()

    fun getSavedNews() = repository.getSavedNews()

    fun deleteArticle(article: ArticleDbModel) = viewModelScope.launch {
        repository.deleteArticle(article)
        isLoading.postValue(false)
    }


}