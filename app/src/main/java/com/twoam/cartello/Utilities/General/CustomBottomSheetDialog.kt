package com.twoam.cartello.Utilities.General


import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.*
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.twoam.cartello.R
import com.twoam.cartello.View.MainActivity
import com.twoam.cartello.View.MedicalPrescriptionsActivity
import com.twoam.cartello.View.MoreActivity
import com.twoam.cartello.View.OrdersActivity

class CustomBottomSheetDialog : BottomSheetDialogFragment(), IBottomSheetCallback{



    internal var view: ViewGroup? = null
    lateinit var layout: RelativeLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        view = inflater.inflate(R.layout.bottom_sheet_layout, container, false) as ViewGroup
        layout = view!!.findViewById(R.id.rlOptions)

        init()
        return view
    }


    fun init() {
        var home = layout.findViewById<LinearLayout>(R.id.lLHome)
        var medical = layout.findViewById<LinearLayout>(R.id.lLMedicalPrescriptions)
        var order = layout.findViewById<LinearLayout>(R.id.lLOrder)
        var more = layout.findViewById<LinearLayout>(R.id.lLMore)


        home.setOnClickListener({
            this.dismiss()
            startActivity(Intent(AppController.getContext(), MainActivity::class.java).putExtra(AppConstants.CURRENTINDEXTAG,0))
        })
        medical.setOnClickListener({
            this.dismiss()
            startActivity(Intent(AppController.getContext(), MedicalPrescriptionsActivity::class.java).putExtra(AppConstants.CURRENTINDEXTAG,1))
        })
        order.setOnClickListener({
            this.dismiss()
            startActivity(Intent(AppController.getContext(), OrdersActivity::class.java).putExtra(AppConstants.CURRENTINDEXTAG,2))
        })
        more.setOnClickListener({
            this.dismiss()
            startActivity(Intent(AppController.getContext(), MoreActivity::class.java).putExtra(AppConstants.CURRENTINDEXTAG,3))
        })

    }


    override fun onBottomSheetClosed(isClosed: Boolean) {
        //        AppConstants.isClosed = isClosed;
        dialog.dismiss()
    }





}
