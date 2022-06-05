package com.ahmetkarli.haberim.api

import com.ahmetkarli.haberim.helper.Constants
import com.ahmetkarli.haberim.models.NewsResponseModel
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET(Constants.END_POINT)
    suspend fun getNews():Response<NewsResponseModel>
}