package com.ahmetkarli.haberim.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ahmetkarli.haberim.models.Article
import com.ahmetkarli.haberim.models.ArticleDbModel

@Dao
interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(article: ArticleDbModel):Long

    @Query("SELECT * FROM articles")
    fun getAllArticles(): LiveData<List<ArticleDbModel>>

    @Delete
    suspend fun deleteArticle(article:ArticleDbModel)
}