package com.twoam.cartello.Utilities.General


import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v7.widget.CardView
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.twoam.cartello.R
import com.twoam.cartello.Utilities.DB.PreferenceController
import com.twoam.cartello.View.*
import kotlinx.android.synthetic.main.bottom_sheet_close.*
import kotlinx.android.synthetic.main.bottom_sheet_filter.*


class FilterBottomSheetDialog : BottomSheetDialogFragment(), IBottomSheetCallback, View.OnClickListener {


    //region Members
    internal var view: ViewGroup? = null
    lateinit var layout: ConstraintLayout
    private var listener: IBottomSheetCallback? = null
    private var action: Int = 0
    private lateinit var cvHigh: CardView
    private lateinit var cvLow: CardView
    private lateinit var cvCancel: CardView
    private var currentAddresID = 0

    var Action: Int //0 log out 1 delete
        get() {
            return action
        }
        set(action) {
            this.action = action
        }

    var CurrentAddressId: Int
        get() {
            return currentAddresID
        }
        set(index) {
            this.currentAddresID = index
        }
    //endregion

    //region Events
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        view = inflater.inflate(R.layout.bottom_sheet_filter1, container, false) as ViewGroup
        layout = view!!.findViewById(R.id.rlOptions)

        init()
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is IBottomSheetCallback) {
            listener = context
        } else {
            throw ClassCastException(context.toString() + " must implement IBottomSheetCallback.onBottomSheetSelectedItem")
        }
    }

    override fun onBottomSheetSelectedItem(index: Int) {

    }

    override fun onBottomSheetClosed(isClosed: Boolean) {
        dialog.dismiss()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.cvCancel -> {
                this.dismiss()
            }

            R.id.cvHigh -> {
                this.dismiss()
                filterHighToLow()
            }
            R.id.cvLow -> {
                this.dismiss()
                filterLowToHigh()
            }
        }
    }
    //endregion

    //region Helper Functions
    fun init() {

        cvHigh = layout.findViewById(R.id.cvHigh)
        cvLow = layout.findViewById(R.id.cvLow)
        cvCancel = layout.findViewById(R.id.cvCancel)


        cvCancel.setOnClickListener(this)
        cvHigh.setOnClickListener(this)
        cvLow.setOnClickListener(this)



    }

    fun navigate(index: Int) {
        listener?.onBottomSheetSelectedItem(index)
    }

    private fun filterHighToLow() {
        navigate(2)
    }

    private fun filterLowToHigh() {
        navigate(1)

    }
    //endregion

}