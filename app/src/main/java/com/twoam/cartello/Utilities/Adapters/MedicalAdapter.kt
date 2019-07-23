package com.twoam.cartello.Utilities.Adapters

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast

import com.twoam.cartello.Model.Area
import com.twoam.cartello.Model.City
import com.twoam.cartello.Model.MedicalPrescriptions
import com.twoam.cartello.R



import java.util.ArrayList

/**
 * Created by Mokhtar on 6/30/2019.
 */

 class MedicalAdapter(private val context: Context, private val medicalList: ArrayList<MedicalPrescriptions>) : RecyclerView.Adapter<MedicalAdapter.MyViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var medical: MedicalPrescriptions? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicalAdapter.MyViewHolder {

        val view = inflater.inflate(R.layout.medical_layout, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MedicalAdapter.MyViewHolder, position: Int) {
        medical = medicalList[position]
//        holder.tvDate.text = medical!!.medical
        holder.tvDate.text = medical!!.name//+" , ${city.name} , ${area?.name}"
    }

    override fun getItemCount(): Int {
        return medicalList.size
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }


    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tvDate: TextView
        var tvTime: TextView
        var tvOrderId: TextView

        init {

            tvDate = itemView.findViewById(R.id.tvDate)
            tvTime = itemView.findViewById(R.id.tvTime)
            tvOrderId = itemView.findViewById(R.id.tvOrderId)

            itemView.setOnClickListener { v ->

                // get position
                val pos = adapterPosition

                // check if item still exists
                if (pos != RecyclerView.NO_POSITION) {

//                    medical = medicalList[pos]
                    Toast.makeText(v.context, "You clicked " + medical!!.id, Toast.LENGTH_SHORT).show()
//                    // open edit medical activity
//                    context.startActivity(Intent(context, EditDeleteMedicalActivity::class.java)
//                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                            .putExtra("medicalIdPosition", pos))
                }
            }
        }

    }
}
