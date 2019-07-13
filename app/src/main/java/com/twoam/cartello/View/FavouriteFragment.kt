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
class FavouriteFragment : BaseFragment() {

    private lateinit var currentView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        currentView = inflater.inflate(R.layout.fragment_favourite, container, false)

        return currentView
    }

}
