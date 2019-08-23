package com.twoam.cartello.View


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

import com.twoam.cartello.R
import com.twoam.cartello.Utilities.Base.BaseFragment


/**
 * A simple [Fragment] subclass.
 */
class TrackFragment : BaseFragment() {

    //region Memebers

    private var btnTrackOrder: Button? = null
    private var currentView: View? = null
    //endregion
    //region Events
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        currentView = inflater.inflate(R.layout.fragment_track, container, false)

        btnTrackOrder = currentView?.findViewById(R.id.btnTrackOrder)

        btnTrackOrder?.setOnClickListener({
            navigateToActiveOrders()
        })
        return currentView
    }




    //endregion
    //region Helper Functions
    private fun navigateToActiveOrders() {
        //navigate to main activity order tabe
    }
    //endregion


}
