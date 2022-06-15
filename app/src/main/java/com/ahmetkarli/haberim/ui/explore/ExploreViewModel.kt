package com.ahmetkarli.haberim.ui.explore

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
class ExploreViewModel
@Inject
constructor(private val repository: NewsRepository) : ViewModel() {

    val isLoading=MutableLiveData<Boolean>()

    private val _response = MutableLiveData<NewsResponseModel>()
    val responseNews: LiveData<NewsResponseModel>
        get() = _response


    fun getAllNewsBySearch(q:String,from:String,to:String,sortBy:String,page:Int) =viewModelScope.launch{
        repository.getNewsBySearch(q,from, to, sortBy, page).let { response ->
            if(response.isSuccessful){
                _response.postValue(response.body())
                isLoading.postValue(false)
            }else{
                Log.d("TAG", "getAllNewsBySearch error : ${response.code()} ")
            }


        }

    }

}