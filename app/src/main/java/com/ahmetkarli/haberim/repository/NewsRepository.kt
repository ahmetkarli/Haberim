package com.ahmetkarli.haberim.repository

import com.ahmetkarli.haberim.api.ApiService
import com.ahmetkarli.haberim.db.ArticleDatabase
import com.ahmetkarli.haberim.models.Article
import com.ahmetkarli.haberim.models.ArticleDbModel
import javax.inject.Inject

class NewsRepository
@Inject
constructor(private val apiService: ApiService,private val db: ArticleDatabase){

    suspend fun getNews(category:String)=apiService.getNews(category=category)
    suspend fun getNewsBySearch(q:String,from:String,to:String,sortBy:String,page:Int)=apiService.getNewsBySearch(q = q,from = from,to = to, sortBy = sortBy,page = page)

    suspend fun upsert(article: ArticleDbModel)=db.getArticleDao().upsert(article)
    fun getSavedNews() = db.getArticleDao().getAllArticles()
    suspend fun deleteArticle(article: ArticleDbModel) = db.getArticleDao().deleteArticle(article)

}