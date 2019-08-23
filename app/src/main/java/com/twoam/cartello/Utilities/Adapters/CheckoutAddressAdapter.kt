package com.twoam.cartello.Utilities.Adapters

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.twoam.cartello.Model.Address
import com.twoam.cartello.Model.Area
import com.twoam.cartello.Model.City
import com.twoam.cartello.R
import com.twoam.cartello.Utilities.DB.PreferenceController
import com.twoam.cartello.Utilities.General.AppConstants
import com.twoam.cartello.View.EditDeleteAddressActivity
import kotlinx.android.synthetic.main.checkout_items_layout.view.*

import java.util.ArrayList

/**
 * Created by Mokhtar on 6/30/2019.
 */

class CheckoutAddressAdapter(private val context: Context, private val addressList: ArrayList<Address>)
    : RecyclerView.Adapter<CheckoutAddressAdapter.MyViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var address: Address? = Address()
    private lateinit var cities: ArrayList<City>
    private lateinit var areas: ArrayList<Area>
    private var addressValue = ""
    private var addressNameValue = ""
    private var row_index=0


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckoutAddressAdapter.MyViewHolder {

        val view = inflater.inflate(R.layout.checkout_items_layout, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: CheckoutAddressAdapter.MyViewHolder, position: Int) {

        var currentAddress = addressList[position]
        cities = PreferenceController.getInstance(context).getCitiesPref(AppConstants.CITIES_DATA)!!
        var city = cities.find { it.id == currentAddress!!.city_id }
        areas = city?.areas!!
        var area = areas.find { it.id == currentAddress!!.area_id }


        addressValue = if (currentAddress!!.apartment.isNullOrEmpty())
            ""
        else
            currentAddress!!.apartment

        addressValue += if (currentAddress!!.address.isNullOrEmpty())
            ""
        else
            " " + currentAddress!!.address


        addressNameValue = if (currentAddress!!.name.isNullOrEmpty())
            ""
        else
            currentAddress!!.name

        addressNameValue += if (area?.name.isNullOrEmpty())
            ""
        else
            " , " + area?.name

        addressNameValue += if (city.name.isNullOrEmpty())
            ""
        else
            " , " + city.name



        if (position == 0) {
            holder.ivActive.visibility = View.VISIBLE
            holder.ivInActive.visibility = View.INVISIBLE
            holder.tvEdit.isEnabled=true
        } else {
            holder.ivActive.visibility = View.INVISIBLE
            holder.ivInActive.visibility = View.VISIBLE
            holder.tvEdit.isEnabled=false
        }



        holder.tvAddress.text = addressValue  // currentAddress!!.apartment+" ${currentAddress!!.address}"
        holder.tvAddressName.text = addressNameValue //currentAddress!!.name + " , ${city.name} , ${area?.name}"

        //update selected item icon on select
        holder.itemView.setOnClickListener(View.OnClickListener {
            row_index=position
            notifyDataSetChanged()
        })

        if(row_index==position)
        {
            holder.ivActive.visibility = View.VISIBLE
            holder.ivInActive.visibility = View.INVISIBLE
            holder.tvEdit.isEnabled=true
            address = addressList[position]
            AppConstants.CurrentSelectedAddress=address!!
        }
        else
        {
            holder.ivActive.visibility = View.INVISIBLE
            holder.ivInActive.visibility = View.VISIBLE
            holder.tvEdit.isEnabled=false
            address = addressList[position]
            AppConstants.CurrentSelectedAddress=address!!
        }

    }

    override fun getItemCount(): Int {
        return addressList.size
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }


    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tvAddress: TextView
        var tvAddressName: TextView
        var tvEdit: TextView
        var ivActive: ImageView
        var ivInActive: ImageView


        init {

            tvAddress = itemView.findViewById(R.id.tvAddress)
            tvAddressName = itemView.findViewById(R.id.tvAddressType)
            tvEdit = itemView.findViewById(R.id.tvEdit)
            ivActive = itemView.findViewById(R.id.ivActive)
            ivInActive = itemView.findViewById(R.id.ivInActive)

            itemView.setOnClickListener({
                // get position
                val pos = adapterPosition

                // check if item still exists
                if (pos != RecyclerView.NO_POSITION) {
                    address = addressList[pos]
                    itemView.ivActive.visibility = View.VISIBLE
                }
            })
            tvEdit.setOnClickListener { v ->

                // get position
                val pos = adapterPosition

                // check if item still exists
                if (pos != RecyclerView.NO_POSITION) {

                    address = addressList[pos]
                    // open edit address activity
                    context.startActivity(Intent(context, EditDeleteAddressActivity::class.java)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            .putExtra("addressIdPosition", pos))
                }
            }
        }

    }
}
