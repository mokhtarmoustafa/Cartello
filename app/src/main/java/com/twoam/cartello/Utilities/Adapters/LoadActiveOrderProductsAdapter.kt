package com.twoam.cartello.Utilities.Adapters

import android.content.Context
import android.content.Intent
import android.support.v4.app.FragmentManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.twoam.cartello.Model.Product
import com.twoam.cartello.R
import com.twoam.cartello.Utilities.General.AppConstants
import com.twoam.cartello.View.MedicalPrescriptionsDetailActivity
import com.twoam.cartello.View.ProductDetailsActivity
import java.util.*

/**
 * Created by Mokhtar on 6/30/2019.
 */

class LoadActiveOrderProductsAdapter(private val fragmentManager: FragmentManager?,
                                     private val context: Context, private val productList: ArrayList<Product>)
    : RecyclerView.Adapter<LoadActiveOrderProductsAdapter.MyViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var product: Product? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoadActiveOrderProductsAdapter.MyViewHolder {

        val view = inflater.inflate(R.layout.orders_active_data_layout, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: LoadActiveOrderProductsAdapter.MyViewHolder, position: Int) {
        var firstLine = ""
        var secondLine = ""
        var formattedProductName = ""

        product = productList[position]

        if (product!!.name!!.length > 29) {
            firstLine = product!!.name!!.substring(0, 29)
            secondLine = product!!.name!!.substring(29)
            formattedProductName = firstLine + "\n" + secondLine
            holder.tvProduct.text = formattedProductName
        }

        else
            holder.tvProduct.text = product!!.name



        holder.tvQuantity.text = "x" + product!!.amount.toString()
        holder.tvTotal.text = product!!.price.toString() + " " + context.getString(R.string.currency)

    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }


    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tvProduct: TextView = itemView.findViewById(R.id.tvProduct)
        var tvTotal: TextView = itemView.findViewById(R.id.tvTotal)
        var tvQuantity: TextView = itemView.findViewById(R.id.tvQuantity)


        init {


            itemView.setOnClickListener { v ->

                // get position
                val pos = adapterPosition

                // check if item still exists
                if (pos != RecyclerView.NO_POSITION) {

                    product = productList[pos]
                    AppConstants.CurrentSelectedProduct = product!!

                    context.startActivity(Intent(context, ProductDetailsActivity::class.java)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                           )
                }
            }
        }

    }
}
