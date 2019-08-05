package com.twoam.cartello.Utilities.Adapters

import android.content.Context
import android.support.v4.app.FragmentManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bumptech.glide.Glide.init
import com.twoam.cartello.Model.Order
import com.twoam.cartello.R
import com.twoam.cartello.Utilities.General.AppConstants
import com.twoam.cartello.Utilities.General.IBottomSheetCallback
import com.twoam.cartello.Utilities.General.LoadActiveOrderDataDialog
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Mokhtar on 6/30/2019.
 */

class ActiveOrderAdapter(private val fragmentManager: FragmentManager?,
                         private val context: Context, private val orderList: ArrayList<Order>)
    : RecyclerView.Adapter<ActiveOrderAdapter.MyViewHolder>() {


    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var order: Order? = null
    private var bottomSheet = LoadActiveOrderDataDialog()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActiveOrderAdapter.MyViewHolder {

        val view = inflater.inflate(R.layout.order_active_layout, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: ActiveOrderAdapter.MyViewHolder, position: Int) {


            order = orderList[position]
            val dateTime = order!!.created_at.split(" ")

            val dateFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val date = dateFormatter.parse(order!!.created_at)
            // Get time from date
            val timeFormatter = SimpleDateFormat("h:mm a")
            val time = timeFormatter.format(date)

//            holder.tvOrderId.text = context.getString(R.string.order_id) + " " + order!!.id

            holder.tvDate.text = dateTime[0] + time

            holder.tvTotalAmountValue.text = order!!.total.toString() + " " + context.getString(R.string.currency)




    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }




    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

//        var tvOrderId: TextView = itemView.findViewById(R.id.tvOrderIDValue)
        var tvDate: TextView = itemView.findViewById(R.id.tvOrderDate)
        var tvTotalAmountValue: TextView = itemView.findViewById(R.id.tvTotalAmountValue)


        init {

            itemView.setOnClickListener { v ->

                // get position
                val pos = adapterPosition

                // check if item still exists
                if (pos != RecyclerView.NO_POSITION) {

                    order = orderList[pos]
                    bottomSheet.CurrentOrder = order!!
                    bottomSheet.show(fragmentManager, "Custom Bottom Sheet")
                }
            }
        }

    }
}
