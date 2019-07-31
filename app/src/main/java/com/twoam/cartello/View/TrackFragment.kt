package com.twoam.cartello.View


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.twoam.cartello.R
import com.twoam.cartello.Utilities.Base.BaseFragment


/**
 * A simple [Fragment] subclass.
 */
class TrackFragment : BaseFragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_track, container, false)
    }

}// Required empty public constructor
