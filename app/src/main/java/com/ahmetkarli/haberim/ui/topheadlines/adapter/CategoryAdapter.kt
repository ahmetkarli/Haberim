package com.ahmetkarli.haberim.ui.topheadlines.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ahmetkarli.haberim.R
import com.ahmetkarli.haberim.databinding.ItemNewsCategoryBinding


class CategoryAdapter : RecyclerView.Adapter<CategoryAdapter.MyViewHolder>() {

    val categoryNameList = arrayListOf("Ekonomi","Magazin","Genel","Sağlık","Bilim","Spor","Teknoloji")
    val categoryImageList = arrayListOf(R.drawable.category_business,R.drawable.category_entertainment,R.drawable.category_general,
        R.drawable.category_health,R.drawable.category_science,R.drawable.category_sports,R.drawable.category_technology)

    inner class MyViewHolder(val binding: ItemNewsCategoryBinding):
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(ItemNewsCategoryBinding.inflate(
            LayoutInflater.from(parent.context),parent,false
        ))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var currentCategoryName=categoryNameList[position]
        var currentCategoryImage=categoryImageList[position]

        holder.binding.apply {
            txtCategoryName.text =currentCategoryName
            profileImage.setImageResource(currentCategoryImage)

            txtCategoryName.setOnClickListener {


            }
            profileImage.setOnClickListener {

            }

        }


    }

    override fun getItemCount()=categoryNameList.size

}