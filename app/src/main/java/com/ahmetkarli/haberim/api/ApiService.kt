package com.ahmetkarli.haberim.api

import com.ahmetkarli.haberim.helper.Constants
import com.ahmetkarli.haberim.models.NewsResponseModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET(Constants.END_POINT)
    suspend fun getNews(
        @Query("country")
        countryCode:String = Constants.COUNTRY_CODE,
        @Query("apiKey")
        apiKey:String = Constants.API_KEY,
        @Query("category")
        category:String = ""
    ):Response<NewsResponseModel>

    @GET(Constants.EVERYTHING)
    suspend fun getNewsBySearch(
        @Query("language")
        language:String = Constants.LANGUAGE,
        @Query("apiKey")
        apiKey:String = Constants.API_KEY,
        @Query("q")
        q:String = ""
    ):Response<NewsResponseModel>

}