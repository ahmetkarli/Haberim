package com.ahmetkarli.haberim.di

import com.ahmetkarli.haberim.api.ApiService
import com.ahmetkarli.haberim.helper.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /*@Provides
    fun provideBaseUrl() = Constants.BASE_URL*/

    @Provides
    fun retrofitNews():ApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)

    }


}