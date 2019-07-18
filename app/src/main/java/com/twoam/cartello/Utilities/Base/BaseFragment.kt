package com.twoam.cartello.Utilities.Base

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.RelativeLayout

import com.twoam.cartello.R
import com.twoam.cartello.Utilities.General.AppController


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * create an instance of this fragment.
 */
open class BaseFragment : Fragment(), OnItemClick {



    lateinit var mLoadingDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {

        mLoadingDialog = Dialog(activity!!)
        mLoadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mLoadingDialog.setContentView(R.layout.progress_bar)
        mLoadingDialog.window!!.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT)
        mLoadingDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        mLoadingDialog.setCancelable(false)

        super.onCreate(savedInstanceState)
    }


    override fun onItemClicked(position: Int) {

    }

    fun showAlertDialouge(message: String) {
        var alertDialouge = AlertDialog.Builder(context!!)
                .setMessage(message)
                .setCancelable(true)
                .setPositiveButton(getString(R.string.ok), { dialog, which -> })
        alertDialouge.create()
        alertDialouge.show()
    }

    fun showDialogue() {
        mLoadingDialog.show()
    }

    //hide progress bar Dialogue
    fun hideDialogue() {
        mLoadingDialog.dismiss()
    }


}
