package com.twoam.cartello.Utilities.Adapters

import android.content.Context
import android.support.v4.app.FragmentManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.twoam.cartello.Model.Product
import com.twoam.cartello.R
import com.twoam.cartello.Utilities.General.AppConstants
import java.util.*

/**
 * Created by Mokhtar on 6/30/2019.
 */

class LoadInActiveOrderProductsAdapter(private val fragmentManager: FragmentManager?,
                                       private val context: Context, private val productList: ArrayList<Product>)
    : RecyclerView.Adapter<LoadInActiveOrderProductsAdapter.MyViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var product: Product? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoadInActiveOrderProductsAdapter.MyViewHolder {

        val view = inflater.inflate(R.layout.orders_inactive_data_layout, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: LoadInActiveOrderProductsAdapter.MyViewHolder, position: Int) {

        product = productList[position]

        var firstLine = ""
        var secondLine = ""
        var formattedProductName = ""

        product = productList[position]


        Glide.with(context).load(product!!.image).into(holder.ivProduct)

        if (product!!.name!!.length > 17) {
            firstLine = product!!.name!!.substring(0, 17)
            secondLine = product!!.name!!.substring(17)
            formattedProductName = firstLine + "\n" + secondLine
            holder.tvProductName.text = formattedProductName
        } else
            holder.tvProductName.text = product!!.name

        holder.tvProductPrice.text = product!!.price.toString() + " " + context.getString(R.string.currency)
        holder.tvQuantity.text = product!!.amount.toString()

    }

    override fun getItemCount(

    ): Int {
        return productList.size
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }


    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var ivProduct: ImageView = itemView.findViewById(R.id.ivProduct)
        var tvProductName: TextView = itemView.findViewById(R.id.tvProductName)
        var tvProductPrice: TextView = itemView.findViewById(R.id.tvProductPrice)
        var tvQuantity: TextView = itemView.findViewById(R.id.tvQuantity)


        init {


            itemView.setOnClickListener { v ->

                // get position
                val pos = adapterPosition

                // check if item still exists
                if (pos != RecyclerView.NO_POSITION) {

                    product = productList[pos]
                    AppConstants.CurrentSelectedProduct = product!!

                }
            }
        }

    }
}
