package com.ahmetkarli.haberim.di

import android.content.Context
import androidx.room.Room
import com.ahmetkarli.haberim.api.ApiService
import com.ahmetkarli.haberim.db.ArticleDao
import com.ahmetkarli.haberim.db.ArticleDatabase
import com.ahmetkarli.haberim.helper.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /*@Provides
    fun provideBaseUrl() = Constants.BASE_URL*/

    @Provides
    fun retrofitNews(): ApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)

    }

    @Singleton
    @Provides
    fun provideMyDB(@ApplicationContext context: Context): ArticleDatabase {
        return Room.databaseBuilder(
            context,
            ArticleDatabase::class.java,
            ArticleDatabase.DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }

    @Singleton
    @Provides
    fun provideMyDao(myDB: ArticleDatabase): ArticleDao {
        return myDB.getArticleDao()

    }
}