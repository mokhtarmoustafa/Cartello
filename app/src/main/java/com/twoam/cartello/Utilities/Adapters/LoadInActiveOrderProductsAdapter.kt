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
import com.twoam.cartello.R.id.tvTotal
import com.twoam.cartello.Utilities.General.AppConstants
import com.twoam.cartello.Utilities.General.AppController
import com.twoam.cartello.Utilities.General.LoadActiveOrderDataDialog
import com.twoam.cartello.View.EditDeleteAddressActivity
import com.twoam.cartello.View.ProductDetailActivity
import java.util.*

/**
 * Created by Mokhtar on 6/30/2019.
 */

class LoadInActiveOrderProductsAdapter(private val fragmentManager: FragmentManager?,
                                       private val context: Context, private val productList: ArrayList<Product>)
    : RecyclerView.Adapter<LoadInActiveOrderProductsAdapter.MyViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var product: Product? = null
    private var bottomSheet = LoadActiveOrderDataDialog()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoadInActiveOrderProductsAdapter.MyViewHolder {

        val view = inflater.inflate(R.layout.orders_inactive_data_layout, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: LoadInActiveOrderProductsAdapter.MyViewHolder, position: Int) {

        product = productList[position]


        holder.tvProductName.text = product!!.name
        holder.tvProductPrice.text=product!!.price.toString()
        holder.tvQuantity.text = "${product!!.amount}"

    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }


    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tvProductName: TextView
        var tvProductPrice: TextView
        var tvQuantity: TextView


        init {

            tvProductName = itemView.findViewById(R.id.tvProductName)
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice)
            tvQuantity = itemView.findViewById(R.id.tvQuantity)


            itemView.setOnClickListener { v ->

                // get position
                val pos = adapterPosition

                // check if item still exists
                if (pos != RecyclerView.NO_POSITION) {

                    product = productList[pos]
                    AppConstants.CurrentSelectedProduct = product!!
                    //todo open product details fragment from here
//                    context.startActivity(Intent(context, ProductDetailActivity::class.java)
//                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                            .putExtra("productIdPosition", pos))
                }
            }
        }

    }
}
