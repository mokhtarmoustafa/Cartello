package com.twoam.cartello.Utilities.Adapters

import android.content.Context
import android.support.v4.app.FragmentManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.twoam.cartello.Model.MedicalPrescriptions
import com.twoam.cartello.R
import com.twoam.cartello.Utilities.General.LoadMedicalDataDialog
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Mokhtar on 6/30/2019.
 */

class MedicalAdapter(private val fragmentManager: FragmentManager?,
                     private val context: Context, private val medicalList: ArrayList<MedicalPrescriptions>)
    : RecyclerView.Adapter<MedicalAdapter.MyViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var medical: MedicalPrescriptions? = null
    private var bottomSheet = LoadMedicalDataDialog()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicalAdapter.MyViewHolder {

        val view = inflater.inflate(R.layout.medical_layout, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MedicalAdapter.MyViewHolder, position: Int) {

        medical = medicalList[position]
        val dateTime = medical!!.created_at.split(" ")

        val dateFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val date = dateFormatter.parse(medical!!.created_at)
        // Get time from date
        val timeFormatter = SimpleDateFormat("h:mm a")
        val time = timeFormatter.format(date)

        holder.tvDate.text = dateTime[0]
        holder.tvTime.text = time
        holder.tvOrderId.text = """${context.getString(R.string.order_id)}${medical!!.id}"""

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

            tvDate = itemView.findViewById(R.id.tvTypeCode)
            tvTime = itemView.findViewById(R.id.tvTime)
            tvOrderId = itemView.findViewById(R.id.tvOrderId)

            itemView.setOnClickListener { v ->

                // get position
                val pos = adapterPosition

                // check if item still exists
                if (pos != RecyclerView.NO_POSITION) {

                    medical = medicalList[pos]
                    bottomSheet.CurrentMedical = medical!!
                    bottomSheet.show(fragmentManager, "Custom Bottom Sheet")
                }
            }
        }

    }
}
