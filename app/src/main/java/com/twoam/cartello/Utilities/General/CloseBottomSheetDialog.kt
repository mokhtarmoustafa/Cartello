package com.twoam.cartello.Utilities.General


import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.twoam.cartello.R
import com.twoam.cartello.R.string.more
import com.twoam.cartello.Utilities.DB.PreferenceController
import com.twoam.cartello.View.*
import kotlin.math.log

class CloseBottomSheetDialog : BottomSheetDialogFragment(), IBottomSheetCallback {


    internal var view: ViewGroup? = null
    lateinit var layout: RelativeLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        view = inflater.inflate(R.layout.bottom_sheet_close, container, false) as ViewGroup
        layout = view!!.findViewById(R.id.rlOptions)

        init()
        return view
    }


    fun init() {
        var ivClose = layout.findViewById<ImageView>(R.id.ivClose)
        var btnLogOut = layout.findViewById<Button>(R.id.btnLogOut)



        ivClose.setOnClickListener({
            this.dismiss()
        })
        btnLogOut.setOnClickListener({
            this.dismiss()
          logOut()
        })
    }


    override fun onBottomSheetClosed(isClosed: Boolean) {
        dialog.dismiss()
    }



    private fun logOut()
    {
        PreferenceController.getInstance(AppController.getContext()).Set(AppConstants.IS_LOGIN, AppConstants.FALSE)
        context?.startActivity(Intent(context, LoginActivity::class.java))
        activity?.onBackPressed()
    }





}
