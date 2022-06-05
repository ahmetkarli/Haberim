package com.ahmetkarli.haberim.ui.topheadlines


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetkarli.haberim.models.NewsResponseModel
import com.ahmetkarli.haberim.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TopHeadlinesViewModel
@Inject
constructor(private val repository: NewsRepository) : ViewModel() {

    private val _response = MutableLiveData<NewsResponseModel>()
    val responseNews:LiveData<NewsResponseModel>
        get() = _response

    init {
        getAllNews()
    }

    private fun getAllNews() =viewModelScope.launch{
        repository.getNews().let { response ->
            if(response.isSuccessful){
                _response.postValue(response.body())
            }else{
                Log.d("TAG", "getAllNews error : ${response.code()} ")
            }


        }

    }
}