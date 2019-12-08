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
import com.twoam.cartello.Model.Order
import com.twoam.cartello.R
import com.twoam.cartello.Utilities.Interfaces.IBottomSheetCallback
import com.twoam.cartello.Utilities.Interfaces.IOrderCallback
import com.twoam.cartello.Utilities.DB.PreferenceController
import com.twoam.cartello.View.*


class CloseBottomSheetDialog : BottomSheetDialogFragment(), IBottomSheetCallback, IOrderCallback, View.OnClickListener {


    //region Members

    internal var view: ViewGroup? = null
    lateinit var layout: RelativeLayout
    private var closeListener: IBottomSheetCallback? = null
    private var cancelOrderListener: IOrderCallback? = null

    private var action: Int = 0
    private lateinit var btnLogOut: Button
    private lateinit var btnDelete: Button
    private lateinit var tvHome: TextView
    private lateinit var ivClose: ImageView
    private var order = Order()

    var Action: Int //0 log out 1 delete 2 cancel order
        get() {
            return action
        }
        set(action) {
            this.action = action
        }

    var CurrentOrder: Order
        get() {
            return order
        }
        set(currentOrder) {
            this.order = currentOrder
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
        if (action == 1) {
            if (context is IBottomSheetCallback) {
                closeListener = context
            } else {
                throw ClassCastException(context.toString() + " must implement IBottomSheetCallback.onBottomSheetSelectedItem")
            }
        } else if (action == 2) {
            if (context is IOrderCallback) {
                cancelOrderListener = context
            } else {
                throw ClassCastException(context.toString() + " must implement IOrderCallback.onCancelOrder")
            }
        }

    }

    override fun onOrderCancelled(isCanceled: Boolean, order: Order?) {

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
                if (action == 1)
                    logOut()
                if (action == 2)
                    cancelOrder(CurrentOrder)
            }
            R.id.btnDelete -> {
                this.dismiss()
                navigate(action)
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
        } else if (action == 2) //cancel order
        {
            btnLogOut.text = context!!.getString(R.string.cancel_order)
            btnLogOut.visibility = View.VISIBLE
            btnDelete.visibility = View.GONE
            tvHome.text = getString(R.string.cancel_order_message)
        }


        ivClose.setOnClickListener(this)
        btnLogOut.setOnClickListener(this)
        btnDelete.setOnClickListener(this)


    }

    fun navigate(index: Int) {
        closeListener?.onBottomSheetSelectedItem(index)
    }

    private fun logOut() {
        PreferenceController.getInstance(AppController.getContext()).clear(AppConstants.IS_LOGIN)
        PreferenceController.getInstance(AppController.getContext()).clear(AppConstants.USER_DATA)
        context?.startActivity(Intent(context, LoginActivity::class.java))
        activity?.onBackPressed()

    }

    private fun cancelOrder(order: Order) {
        cancelOrderListener?.onOrderCancelled(true, order)
    }


//endregion
}
