package com.twoam.cartello.Utilities.Adapters

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.twoam.cartello.Model.SubCategory
import com.twoam.cartello.R
import com.twoam.cartello.Utilities.General.AppConstants
import com.twoam.cartello.View.ProductDetailsActivity
import com.twoam.cartello.View.SearchResultActivity

import java.util.ArrayList

/**
 * Created by Mokhtar on 6/30/2019.
 */

class SubCategoryAdapter(private val context: Context, private val imageModelArrayList: ArrayList<SubCategory>) : RecyclerView.Adapter<SubCategoryAdapter.MyViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubCategoryAdapter.MyViewHolder {

        val view = inflater.inflate(R.layout.recycler_sub_category, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: SubCategoryAdapter.MyViewHolder, position: Int) {
        //use glide
        Glide.with(context).load(imageModelArrayList[position].image)
                .apply(RequestOptions.placeholderOf(R.drawable.item))
                .thumbnail(0.1f)
                .into(holder.itemImage)
        holder.itemName.text = imageModelArrayList[position].name


    }

    override fun getItemCount(): Int {
        return imageModelArrayList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var itemName: TextView
        var itemImage: ImageView

        init {

            itemName = itemView.findViewById(R.id.tvItemName)
            itemImage = itemView.findViewById(R.id.ivItemImage)

            itemView.setOnClickListener { v ->
                // get position
                val pos = adapterPosition

                // check if item still exists
                if (pos != RecyclerView.NO_POSITION) {
                    var subCategory = imageModelArrayList[pos]
                    AppConstants.CurrentSelectedSubCategory=subCategory

                    context.startActivity(Intent(context, SearchResultActivity::class.java).putExtra(AppConstants.SEARCH_SUB_CATEGORY, subCategory.id.toString()))

                }
            }
        }
    }
}
