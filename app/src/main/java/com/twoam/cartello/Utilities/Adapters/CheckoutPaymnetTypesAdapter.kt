package com.twoam.cartello.Utilities.Adapters

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.twoam.cartello.Model.Cart
import com.twoam.cartello.Model.PaymentType
import com.twoam.cartello.R
import com.twoam.cartello.R.id.rlParent
import com.twoam.cartello.View.PaymentFragment
import kotlinx.android.synthetic.main.bottom_sheet_active_order.*
import kotlinx.android.synthetic.main.fragment_payment.*
import kotlinx.android.synthetic.main.payment_types_items_layout.view.*
import java.text.NumberFormat

import java.util.ArrayList

/**
 * Created by Mokhtar on 6/30/2019.
 */

class CheckoutPaymnetTypesAdapter(private val context: Context, private val paymentTypeList: ArrayList<PaymentType>, private var paymentTypesFragment: PaymentFragment)
    : RecyclerView.Adapter<CheckoutPaymnetTypesAdapter.MyViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var paymentType: PaymentType? = PaymentType()
    private var row_index = 0
    private var paymentFragment: PaymentFragment = PaymentFragment()
    private val deliveryFees="15 LE"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckoutPaymnetTypesAdapter.MyViewHolder {

        val view = inflater.inflate(R.layout.payment_types_items_layout, parent, false)
        this.paymentFragment = paymentTypesFragment
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: CheckoutPaymnetTypesAdapter.MyViewHolder, position: Int) {

        var currentPaymentType = paymentTypeList[position]


        holder.tvPaymentType.text = currentPaymentType?.name  // currentPaymentType!!.apartment+" ${currentPaymentType!!.paymentType}"
        paymentTypesFragment.tvDeliveryFess.text = context.getString(R.string.delivery_fess) + " " + deliveryFees
        //remove the decimal fractions if it 0
        var total = NumberFormat.getInstance().format(Cart.getTotal())
        paymentTypesFragment.btnPlaceOrder.text = context.getString(R.string.place_order) + " " + total+ " " + context.getString(R.string.currency)


        //update selected item icon on select
        holder.itemView.setOnClickListener(View.OnClickListener {
            row_index = position
            notifyDataSetChanged()
        })


        if (position == 0) {

            holder.ivInActive.visibility = View.VISIBLE
            holder.ivActive.visibility = View.INVISIBLE
            holder.rlParent.setBackgroundColor(Color.parseColor("#0f000000"))
        }

        holder.itemView.setOnClickListener(View.OnClickListener {
            row_index = 1
            notifyDataSetChanged()
        })

        if (row_index == 1) {
            holder.ivActive.visibility = View.VISIBLE
            holder.rlParent.setBackgroundColor(Color.parseColor("#FFFFFF"))
            holder.ivInActive.visibility = View.INVISIBLE

            paymentTypesFragment.btnPlaceOrder.isEnabled = true
            paymentTypesFragment.btnPlaceOrder.alpha = 1F


        } else {
            holder.ivInActive.visibility = View.VISIBLE
            holder.ivActive.visibility = View.INVISIBLE
        }


    }

    override fun getItemCount(): Int {
        return paymentTypeList.size
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }


    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tvPaymentType: TextView
        var ivActive: ImageView
        var ivInActive: ImageView
        var rlParent: RelativeLayout


        init {

            tvPaymentType = itemView.findViewById(R.id.tvPaymentType)
            ivActive = itemView.findViewById(R.id.ivActive)
            ivInActive = itemView.findViewById(R.id.ivInActive)
            rlParent = itemView.findViewById(R.id.rlParent)

            itemView.setOnClickListener({
                // get position
                val pos = adapterPosition

                // check if item still exists
                if (pos != RecyclerView.NO_POSITION) {
                    paymentType = paymentTypeList[pos]
                    itemView.ivActive.visibility = View.VISIBLE
                    itemView.ivInActive.visibility = View.INVISIBLE

                }
            })
        }

    }
}
