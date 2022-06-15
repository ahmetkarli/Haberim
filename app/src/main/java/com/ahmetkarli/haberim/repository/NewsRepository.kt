package com.ahmetkarli.haberim.repository

import com.ahmetkarli.haberim.api.ApiService
import javax.inject.Inject

class NewsRepository
@Inject
constructor(private val apiService: ApiService){

    suspend fun getNews(category:String)=apiService.getNews(category=category)
    suspend fun getNewsBySearch(q:String,from:String,to:String,sortBy:String,page:Int)=apiService.getNewsBySearch(q = q,from = from,to = to, sortBy = sortBy,page = page)
}