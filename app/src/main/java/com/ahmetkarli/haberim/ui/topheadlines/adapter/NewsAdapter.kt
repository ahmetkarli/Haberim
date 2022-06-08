package com.ahmetkarli.haberim.ui.topheadlines.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.ahmetkarli.haberim.databinding.ItemNewsBinding
import com.ahmetkarli.haberim.models.Article
import com.ahmetkarli.haberim.models.NewsResponseModel
import com.bumptech.glide.Glide


class NewsAdapter : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    inner class NewsViewHolder(val binding: ItemNewsBinding):
        RecyclerView.ViewHolder(binding.root)

    private val differCallback=object:DiffUtil.ItemCallback<Article>(){
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url==newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem==newItem
        }


    }

    private val differ = AsyncListDiffer(this,differCallback)
    var newsList : List<Article>
         get()=differ.currentList
        set(value){
            differ.submitList(value)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {

        return NewsViewHolder(
            ItemNewsBinding.inflate(
            LayoutInflater.from(parent.context),parent,false
        ))


    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val currentArticle = newsList[position]

        currentArticle.let {
            holder.binding.apply {
                txtTitle.text=currentArticle.title
                txtDate.text=dateFormat(currentArticle.publishedAt.toString())
                imgArticle.load(currentArticle.urlToImage){
                    crossfade(true)
                    crossfade(1000)
                }

            }
        }

    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    private fun dateFormat(date:String):String{
       // 2022-06-08T07:58:38Z
        val day = date.substring(8,10)
        val month = date.substring(5,7)
        val year = date.substring(0,4)
        var hour = date.substring(11,13).toInt()
        if(hour<21){
            hour += 3
        }else {
            when(hour){
                21->hour=0
                22->hour=1
                23->hour=2
                24->hour=3
            }
        }

        val minute = date.substring(13,19)
        val newDate = day+"-"+month+"-"+year+" "+hour+minute

        return newDate
    }
}


