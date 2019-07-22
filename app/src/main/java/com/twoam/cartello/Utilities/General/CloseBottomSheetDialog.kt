package com.twoam.cartello.Utilities.General


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.twoam.cartello.R
import com.twoam.cartello.Utilities.DB.PreferenceController
import com.twoam.cartello.View.*
import kotlinx.android.synthetic.main.bottom_sheet_close.*


class CloseBottomSheetDialog : BottomSheetDialogFragment(), IBottomSheetCallback, View.OnClickListener {


    //region Members
    internal var view: ViewGroup? = null
    lateinit var layout: RelativeLayout
    private var listener: IBottomSheetCallback? = null
    private var action: Int = 0
    private lateinit var btnLogOut: Button
    private lateinit var btnDelete: Button
    private lateinit var tvHome: TextView
    private lateinit var ivClose: ImageView

    var Action: Int //0 log out 1 delete
        get() {
            return action
        }
        set(action) {
            this.action = action
        }

    //endregion

    //region Events
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        view = inflater.inflate(R.layout.bottom_sheet_close, container, false) as ViewGroup
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
            R.id.ivClose -> {
                this.dismiss()
            }

            R.id.btnLogOut -> {
                this.dismiss()
                logOut()
            }
            R.id.btnDelete -> {
                this.dismiss()
                navigate(1)
            }
        }
    }
    //endregion

    //region Helper Functions
    fun init() {

        btnLogOut = layout.findViewById(R.id.btnLogOut)
        btnDelete = layout.findViewById(R.id.btnDelete)
        tvHome = layout.findViewById(R.id.tvHome)
        ivClose = layout.findViewById(R.id.ivClose)

        if (Action == 0) //logout
        {
            btnLogOut.visibility = View.VISIBLE
            btnDelete.visibility = View.GONE
            tvHome.text = getString(R.string.sure_you_want_to_log_out)
        } else if (Action == 1)//delet address
        {
            btnLogOut.visibility = View.GONE
            btnDelete.visibility = View.VISIBLE
            tvHome.text = getString(R.string.sure_you_want_to_proceed)
        }


//        var ivClose = layout.findViewById<ImageView>(R.id.ivClose)
//        var btnLogOut = layout.findViewById<Button>(R.id.btnLogOut)
//        var btnDelete=layout.findViewById<Button>(R.id.btnDelete)

        ivClose.setOnClickListener(this)
        btnLogOut.setOnClickListener(this)
        btnDelete.setOnClickListener(this)
//
//

//        if (Action == 0) { //logout
//            btnLogOut.setOnClickListener({
//                this.dismiss()
//                logOut()
//            })
//        } else if (Action == 1) { // delete
//
//            btnLogOut.text = getString(R.string.delete_my_address)
//            btnLogOut.setBackgroundColor(Color.parseColor("#F9ECEC"))
//            btnLogOut.setTextColor(Color.parseColor("#FF0202"))
//            btnLogOut.setOnClickListener({
//                this.dismiss()
//                navigate(Action)
//            })
//
//        }

    }

    fun navigate(index: Int) {
        listener?.onBottomSheetSelectedItem(index)
    }

    private fun logOut() {
        PreferenceController.getInstance(AppController.getContext()).clear(AppConstants.IS_LOGIN)
        PreferenceController.getInstance(AppController.getContext()).clear(AppConstants.USER_DATA)
        context?.startActivity(Intent(context, LoginActivity::class.java))
        activity?.onBackPressed()
    }
    //endregion

}