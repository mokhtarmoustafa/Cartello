package com.twoam.cartello.Utilities.Adapters

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast

import com.twoam.cartello.Model.Address
import com.twoam.cartello.Model.Area
import com.twoam.cartello.Model.City
import com.twoam.cartello.R
import com.twoam.cartello.Utilities.DB.PreferenceController
import com.twoam.cartello.Utilities.General.AppConstants
import com.twoam.cartello.View.EditDeleteAddressActivity

import java.util.ArrayList

/**
 * Created by Mokhtar on 6/30/2019.
 */

class AddressAdapter(private val context: Context, private val addressList: ArrayList<Address>) : RecyclerView.Adapter<AddressAdapter.MyViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var address: Address? = null
    private lateinit var cities: ArrayList<City>
    private lateinit var areas: ArrayList<Area>
    var addressValue = ""
    var addresssNameVAlue = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressAdapter.MyViewHolder {

        val view = inflater.inflate(R.layout.address_layout, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: AddressAdapter.MyViewHolder, position: Int) {
//        address = addressList[position]
        var currentAddress = addressList[position]
        cities = PreferenceController.getInstance(context).getCitiesPref(AppConstants.CITIES_DATA)!!
        var city = cities.find { it.id == currentAddress!!.city_id }
        areas = city?.areas!!
        var area = areas.find { it.id == currentAddress!!.area_id }


        addressValue = if (currentAddress!!.apartment.isNullOrEmpty())
            ""
        else
            currentAddress!!.apartment

        addressValue+=if(currentAddress!!.address.isNullOrEmpty())
            ""
        else
            " "+currentAddress!!.address


        addresssNameVAlue = if (currentAddress!!.name.isNullOrEmpty())
            ""
        else
            currentAddress!!.name

        addresssNameVAlue+=if(city.name.isNullOrEmpty())
            ""
        else
            " , "+city.name

        addresssNameVAlue+=if(area?.name.isNullOrEmpty())
            ""
        else
            " , "+area?.name



        holder.tvAddress.text =addressValue  // currentAddress!!.apartment+" ${currentAddress!!.address}"
        holder.tvAddressName.text = addresssNameVAlue //currentAddress!!.name + " , ${city.name} , ${area?.name}"
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

        init {

            tvAddress = itemView.findViewById(R.id.tvAddress)
            tvAddressName = itemView.findViewById(R.id.tvAddressType)
            tvEdit = itemView.findViewById(R.id.tvEdit)

            tvEdit.setOnClickListener { v ->

                // get position
                val pos = adapterPosition

                // check if item still exists
                if (pos != RecyclerView.NO_POSITION) {

                    address = addressList[pos]
                    Toast.makeText(v.context, "You clicked " + address!!.address, Toast.LENGTH_SHORT).show()
                    // open edit address activity
                    context.startActivity(Intent(context, EditDeleteAddressActivity::class.java)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            .putExtra("addressIdPosition", pos))
                }
            }
        }

    }
}
