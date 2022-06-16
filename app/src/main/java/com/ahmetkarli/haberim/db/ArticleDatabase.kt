package com.ahmetkarli.haberim.db


import androidx.room.Database
import androidx.room.RoomDatabase
import com.ahmetkarli.haberim.models.ArticleDbModel

@Database(
    entities = [ArticleDbModel::class],
    version = 1
)
abstract class ArticleDatabase : RoomDatabase(){

    abstract fun getArticleDao():ArticleDao

    companion object{
        val DATABASE_NAME = "articles_db.db"

    }
}