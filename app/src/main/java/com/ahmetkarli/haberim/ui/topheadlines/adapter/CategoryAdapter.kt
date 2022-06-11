package com.ahmetkarli.haberim.ui.topheadlines.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ahmetkarli.haberim.R
import com.ahmetkarli.haberim.databinding.ItemNewsCategoryBinding


class CategoryAdapter(
    private var onItemClicked: ((categoryName: String) -> Unit)
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    val categoryNameList = arrayListOf("Genel","Ekonomi","Spor","Sağlık","Teknoloji","Magazin","Bilim")
    val categoryServiceName = arrayListOf("general","business","sports","health","technology","entertainment","science")
    val categoryImageList = arrayListOf(R.drawable.category_general,R.drawable.category_business,R.drawable.category_sports,
        R.drawable.category_health,R.drawable.category_technology,R.drawable.category_entertainment,R.drawable.category_science)

    inner class CategoryViewHolder(val binding: ItemNewsCategoryBinding):
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(ItemNewsCategoryBinding.inflate(
            LayoutInflater.from(parent.context),parent,false
        ))
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        var currentCategoryName=categoryNameList[position]
        var currentCategoryImage=categoryImageList[position]
        var currentCategoryServiceName=categoryServiceName[position]


        holder.binding.apply {
            txtCategoryName.text =currentCategoryName
            profileImage.setImageResource(currentCategoryImage)

            root.setOnClickListener {
                onItemClicked(currentCategoryServiceName)
            }

        }


    }

    override fun getItemCount()=categoryNameList.size

}