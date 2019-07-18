package com.twoam.cartello.Utilities.General


import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.twoam.cartello.R
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.AdapterView.OnItemSelectedListener


class CustomBottomSheetDialog : BottomSheetDialogFragment(), IBottomSheetCallback {



    internal var view: ViewGroup? = null
    lateinit var layout: RelativeLayout
    private lateinit var tvHomeBS: TextView
    private lateinit var ivHomeBS: ImageView
    private lateinit var tvMedicalBS: TextView
    private lateinit var ivMedicalBS: ImageView
    private lateinit var tvOrderBS: TextView
    private lateinit var ivOrderBS: ImageView
    private lateinit var tvMoreBS: TextView
    private lateinit var ivMoreBS: ImageView
    private lateinit var listener: IBottomSheetCallback

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        view = inflater.inflate(R.layout.bottom_sheet_layout, container, false) as ViewGroup
        layout = view!!.findViewById(R.id.rlOptions)

        init()
        return view
    }

    override fun onBottomSheerSelectedItem(index: Int) {

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is IBottomSheetCallback) {
            listener = context
        } else {
            throw ClassCastException(context.toString() + " must implement IBottomSheetCallback.onBottomSheerSelectedItem")
        }
    }

    fun doAction(index: Int) {
        listener.onBottomSheerSelectedItem(index)
    }

    fun init() {
        var home = layout.findViewById<LinearLayout>(R.id.lLHome)

        tvHomeBS = layout.findViewById<TextView>(R.id.tvHomeBS)
        ivHomeBS = layout.findViewById<ImageView>(R.id.ivHomeBS)
        tvMedicalBS = layout.findViewById<TextView>(R.id.tvMedicalPrescriptionsBS)
        ivMedicalBS = layout.findViewById<ImageView>(R.id.ivMedicalPrescriptionsBS)
        tvOrderBS = layout.findViewById<TextView>(R.id.tvOrderBS)
        ivOrderBS = layout.findViewById<ImageView>(R.id.ivOrderBS)
        tvMoreBS = layout.findViewById<TextView>(R.id.tvMoreBS)
        ivMoreBS = layout.findViewById<ImageView>(R.id.ivMoreBS)

        var medical = layout.findViewById<LinearLayout>(R.id.lLMedicalPrescriptions)
        var order = layout.findViewById<LinearLayout>(R.id.lLOrder)
        var more = layout.findViewById<LinearLayout>(R.id.lLMore)

        changeControlsSettings(AppConstants.CURRENTSELECTEDINDEX)

        home.setOnClickListener({

            doAction(0)

//            if (AppConstants.CURRENTSELECTEDINDEX == 0) {
//                this.dismiss()
//                return@setOnClickListener
//
//            }
//
//            fragmentManager!!.beginTransaction().replace(R.id.layout_container, HomeFragment()).addToBackStack(null).commit()
//            AppConstants.CURRENTSELECTEDINDEX = 0
//            changeControlsSettings(AppConstants.CURRENTSELECTEDINDEX)
            this.dismiss()
        })
        medical.setOnClickListener({
            doAction(1)
//            if (AppConstants.CURRENTSELECTEDINDEX == 1) {
//                this.dismiss()
//                return@setOnClickListener
//
//            }
//            AppConstants.CURRENTSELECTEDINDEX = 1
//            fragmentManager?.popBackStack()
//            fragmentManager!!.beginTransaction().replace(R.id.layout_container, MedicalPrescriptionsFragment().newInstance()).addToBackStack(null).commit()
//            changeControlsSettings(AppConstants.CURRENTSELECTEDINDEX)
            this.dismiss()
        })
        order.setOnClickListener({
            doAction(2)
//            if (AppConstants.CURRENTSELECTEDINDEX == 2) {
//                this.dismiss()
//                return@setOnClickListener
//
//            }
//            AppConstants.CURRENTSELECTEDINDEX = 2
//            fragmentManager?.popBackStack()
//            fragmentManager!!.beginTransaction().replace(R.id.layout_container, OrdersFragment().newInstance()).addToBackStack(null).commit()
//            changeControlsSettings(AppConstants.CURRENTSELECTEDINDEX)
            this.dismiss()
        })
        more.setOnClickListener({
            doAction(3)
//            if (AppConstants.CURRENTSELECTEDINDEX == 3) {
//                this.dismiss()
//                return@setOnClickListener
//
//            }
//            AppConstants.CURRENTSELECTEDINDEX = 3
//            fragmentManager?.popBackStack()
//            fragmentManager!!.beginTransaction().replace(R.id.layout_container, MoreFragment().newInstance()).addToBackStack(null).commit()
//            changeControlsSettings(AppConstants.CURRENTSELECTEDINDEX)
            this.dismiss()
        })

    }


    override fun onBottomSheetClosed(isClosed: Boolean) {
        // AppConstants.isClosed = isClosed;
        dialog.dismiss()
    }

    private fun changeControlsSettings(index: Int) {
        when (index) {
            0 -> {//HOME
                activity!!.tvMainHome.text = getString(R.string.tab_home)
                tvHomeBS.setTextColor(Color.parseColor("#38a0cd"))
                tvMedicalBS.setTextColor(Color.parseColor("#aaaaaa"))
                tvOrderBS.setTextColor(Color.parseColor("#aaaaaa"))
                tvMoreBS.setTextColor(Color.parseColor("#aaaaaa"))

                ivHomeBS.setImageResource(R.drawable.ic_home_select)
                ivMedicalBS.setImageResource(R.drawable.ic_medical)
                ivOrderBS.setImageResource(R.drawable.ic_orders)
                ivMoreBS.setImageResource(R.drawable.ic_more)
            }
            1 -> {//MEDICAL
                activity!!.tvMainHome.text = getString(R.string.medical_prescriptions)
                tvHomeBS.setTextColor(Color.parseColor("#aaaaaa"))
                tvMedicalBS.setTextColor(Color.parseColor("#38a0cd"))
                tvOrderBS.setTextColor(Color.parseColor("#aaaaaa"))
                tvMoreBS.setTextColor(Color.parseColor("#aaaaaa"))

                ivHomeBS.setImageResource(R.drawable.ic_home)
                ivMedicalBS.setImageResource(R.drawable.ic_medical_select)
                ivOrderBS.setImageResource(R.drawable.ic_orders)
                ivMoreBS.setImageResource(R.drawable.ic_more)
            }
            2 -> {//ORDERS
                activity!!.tvMainHome.text = getString(R.string.orders)

                tvHomeBS.setTextColor(Color.parseColor("#aaaaaa"))
                tvMedicalBS.setTextColor(Color.parseColor("#aaaaaa"))
                tvOrderBS.setTextColor(Color.parseColor("#38a0cd"))
                tvMoreBS.setTextColor(Color.parseColor("#aaaaaa"))

                ivHomeBS.setImageResource(R.drawable.ic_home)
                ivMedicalBS.setImageResource(R.drawable.ic_medical)
                ivOrderBS.setImageResource(R.drawable.ic_orders_select)
                ivMoreBS.setImageResource(R.drawable.ic_more)
            }
            3 ->//MORE
            {
                activity!!.tvMainHome.text = getString(R.string.more)
                tvHomeBS.setTextColor(Color.parseColor("#aaaaaa"))
                tvMedicalBS.setTextColor(Color.parseColor("#aaaaaa"))
                tvOrderBS.setTextColor(Color.parseColor("#aaaaaa"))
                tvMoreBS.setTextColor(Color.parseColor("#38a0cd"))

                ivHomeBS.setImageResource(R.drawable.ic_home)
                ivMedicalBS.setImageResource(R.drawable.ic_medical)
                ivOrderBS.setImageResource(R.drawable.ic_orders)
                ivMoreBS.setImageResource(R.drawable.ic_more_select)
            }


        }
    }

    override fun onCancel(dialog: DialogInterface?) {
        super.onCancel(dialog)
        activity!!.tvMainHome.text = getString(R.string.tab_home)
        AppConstants.CURRENTSELECTEDINDEX = 0
    }




}
