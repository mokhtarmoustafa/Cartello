package com.twoam.cartello.Utilities.Adapters

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

import com.twoam.cartello.Model.Order
import com.twoam.cartello.Model.Area
import com.twoam.cartello.Model.City
import com.twoam.cartello.R
import com.twoam.cartello.Utilities.DB.PreferenceController
import com.twoam.cartello.Utilities.General.AppConstants
import kotlinx.android.synthetic.main.checkout_items_layout.view.*

import java.util.ArrayList

/**
 * Created by Mokhtar on 6/30/2019.
 */

class PaymentTypesAdapter(private val context: Context, private val orderList: ArrayList<Order>) : RecyclerView.Adapter<PaymentTypesAdapter.MyViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var order: Order? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentTypesAdapter.MyViewHolder {

        val view = inflater.inflate(R.layout.payment_types_items_layout, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: PaymentTypesAdapter.MyViewHolder, position: Int) {

        var currentOrder = orderList[position]

        when (currentOrder.payment_method) {
            1 -> {
                holder.tvPaymentType.text = context.getString(R.string.cash)
                holder.ivPaymentType.setImageResource(R.drawable.money)
            }
            2 -> {
                holder.tvPaymentType.text = context.getString(R.string.credit_card)
                holder.ivPaymentType.setImageResource(R.drawable.visa)
            }
            3 -> {
                holder.tvPaymentType.text = context.getString(R.string.messa)
                holder.ivPaymentType.setImageResource(R.drawable.mezza)
            }


        }
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }


    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        var tvPaymentType: TextView = itemView.findViewById(R.id.tvPaymentType)
        var ivPaymentType: ImageView = itemView.findViewById(R.id.ivPaymentType)
        var ivActive: ImageView = itemView.findViewById(R.id.ivActive)
        var ivInActive: ImageView = itemView.findViewById(R.id.ivInActive)


        init {

            itemView.setOnClickListener({
                // get position
                val pos = adapterPosition

                // check if item still exists
                if (pos != RecyclerView.NO_POSITION) {
                    order = orderList[pos]
                    itemView.ivActive.visibility = View.VISIBLE
                }
            })
            ivPaymentType.setOnClickListener { v ->

                // get position
                val pos = adapterPosition

                // check if item still exists
                if (pos != RecyclerView.NO_POSITION) {

                    order = orderList[pos]
                    // open edit order activity
                    //todo proceed to the next  track action
                }
            }
        }

    }
}
