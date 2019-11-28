package com.twoam.cartello.Utilities.Adapters

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.twoam.cartello.Model.Search
import com.twoam.cartello.R
import com.twoam.cartello.Utilities.General.AppConstants
import com.twoam.cartello.View.SearchResultActivity

import java.util.ArrayList

/**
 * Created by Mokhtar on 6/30/2019.
 */

class SearchAdapter(private val context: Context, private val searchList: ArrayList<Search>) : RecyclerView.Adapter<SearchAdapter.MyViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchAdapter.MyViewHolder {

        val view = inflater.inflate(R.layout.search_layout, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchAdapter.MyViewHolder, position: Int) {
        //use glide
        holder.itemName.text = searchList[position].name


    }

    override fun getItemCount(): Int {
        return searchList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var itemName: TextView = itemView.findViewById(R.id.tvItemName)

        init {

            itemView.setOnClickListener { v ->
                // get position
                val pos = adapterPosition

                // check if item still exists
                if (pos != RecyclerView.NO_POSITION) {
                    var search = searchList[pos]
                    context.startActivity(Intent(context, SearchResultActivity::class.java).putExtra(AppConstants.SEARCH_DATA, search.name))

                }
            }

        }
    }
}
