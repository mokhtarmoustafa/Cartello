package com.twoam.cartello.Utilities.Adapters

import android.content.Context
import android.support.v4.app.FragmentManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.twoam.cartello.Model.Order
import com.twoam.cartello.R
import com.twoam.cartello.R.id.tvOrderId
import com.twoam.cartello.R.id.tvTime
import com.twoam.cartello.Utilities.General.*
import com.twoam.cartello.View.OrdersFragment
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Mokhtar on 6/30/2019.
 */

class ActiveOrdersAdapter(private val fragmentManager: FragmentManager?,
                          private val context: Context, private val orderList: ArrayList<Order>)
    : RecyclerView.Adapter<ActiveOrdersAdapter.MyViewHolder>() {



    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var order: Order? = null
    private var bottomSheet = LoadActiveOrderDataDialog()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActiveOrdersAdapter.MyViewHolder {

        val view = inflater.inflate(R.layout.order_active_layout, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: ActiveOrdersAdapter.MyViewHolder, position: Int) {

        order = orderList[position]

        if (orderList.count() == 1 && order?.id == 0) {
            holder.tvEmptyOrder.visibility = View.VISIBLE
            holder.tvOrderId.visibility = View.GONE
            holder.tvDate.visibility = View.GONE
            holder.tvTotalAmount.visibility = View.GONE
            holder.tvTotalAmountValue.visibility = View.GONE
            holder.tvOrderId.visibility = View.GONE
            holder.tvPaymentType.visibility = View.GONE
            holder.tvPaymentTypeValue.visibility = View.GONE

        } else {
            holder.tvEmptyOrder.visibility = View.GONE
            holder.tvOrderId.visibility = View.VISIBLE
            holder.tvDate.visibility = View.VISIBLE
            holder.tvTotalAmount.visibility = View.VISIBLE
            holder.tvTotalAmountValue.visibility = View.VISIBLE
            holder.tvOrderId.visibility = View.VISIBLE
            holder.tvPaymentType.visibility = View.VISIBLE
            holder.tvPaymentTypeValue.visibility = View.VISIBLE


            val dateTime = order!!.date.split(" ")

            val dateFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val date = dateFormatter.parse(order!!.date)
            // Get time from date
            val timeFormatter = SimpleDateFormat("h:mm a")
            val time = timeFormatter.format(date)

            holder.tvOrderId.text = context.getString(R.string.order_id) + "  " + order!!.id.toString()
            holder.tvDate.text = dateTime[0] + " " + time
            holder.tvTotalAmountValue.text = "${order!!.total}${context.getString(R.string.currency)}"

        }
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }






    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tvOrderId: TextView = itemView.findViewById(R.id.tvOrderID)
        var tvDate: TextView = itemView.findViewById(R.id.tvOrderDate)
        var tvTotalAmountValue: TextView = itemView.findViewById(R.id.tvTotalAmountValue)
        var tvTotalAmount: TextView = itemView.findViewById(R.id.tvTotalAmount)
        var tvEmptyOrder: TextView = itemView.findViewById(R.id.tvEmptyOrder)
        var tvPaymentType: TextView = itemView.findViewById(R.id.tvPaymentType)
        var tvPaymentTypeValue: TextView = itemView.findViewById(R.id.tvPaymentTypeValue)

        init  {

            itemView.setOnClickListener { v ->

                // get position
                val pos = adapterPosition

                // check if item still exists
                if (pos != RecyclerView.NO_POSITION) {

                    order = orderList[pos]
                    bottomSheet.CurrentOrder = order!!

                    if(order!!.id>0)
                    bottomSheet.show(fragmentManager, "Custom Bottom Sheet")
                }
            }
        }

    }
}
