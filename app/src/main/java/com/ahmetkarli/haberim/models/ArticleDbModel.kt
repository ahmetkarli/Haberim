package com.ahmetkarli.haberim.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(
    tableName = "articles"
)
data class ArticleDbModel(
    @PrimaryKey(autoGenerate = true)
    var id:Int? = null,
    val publishedAt: String?,
    val title: String?,
    val url: String?,
    val urlToImage: String?
) : Serializable
