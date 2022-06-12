package com.ahmetkarli.haberim.repository

import com.ahmetkarli.haberim.api.ApiService
import javax.inject.Inject

class NewsRepository
@Inject
constructor(private val apiService: ApiService){

    suspend fun getNews(category:String)=apiService.getNews(category=category)
    suspend fun getNewsBySearch(q:String)=apiService.getNewsBySearch(q=q)
}