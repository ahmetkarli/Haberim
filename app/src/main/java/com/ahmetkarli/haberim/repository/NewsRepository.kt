package com.ahmetkarli.haberim.repository

import com.ahmetkarli.haberim.api.ApiService
import javax.inject.Inject

class NewsRepository
@Inject
constructor(private val apiService: ApiService){

    suspend fun getNews()=apiService.getNews()
}