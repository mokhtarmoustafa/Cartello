package com.twoam.cartello.View


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.CardView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.twoam.cartello.R


class MoreFragment : Fragment(), View.OnClickListener {

    //region Members
    private var currentView: View? = null
    private var cvFavourite: CardView? = null
    private var cvProfile: CardView? = null


    //endregion

    //region Events
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.cvProfile -> {
                startActivity(Intent(context, ProfileActivity::class.java))
            }
            R.id.cvFavourite -> {
                fragmentManager!!.beginTransaction().replace(R.id.layout_container, FavouriteFragment()).commit()
            }
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        currentView = inflater.inflate(R.layout.fragment_more, container, false)

        cvFavourite = currentView?.findViewById(R.id.cvFavourite)
        cvProfile = currentView?.findViewById(R.id.cvProfile)

        cvFavourite?.setOnClickListener(this)
        cvProfile?.setOnClickListener(this)


        return currentView
    }
    //endregion

    //region Helper unctions


    //endregion


}
