package com.twoam.cartello.Utilities.General


import android.content.Context
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.*
import com.twoam.cartello.Model.Order
import com.twoam.cartello.Model.Product
import com.twoam.cartello.R
import com.twoam.cartello.Utilities.Adapters.LoadActiveOrderProductsAdapter


class LoadActiveOrderDataDialog : BottomSheetDialogFragment(), IBottomSheetCallback {


    //region Members
    internal var view: ViewGroup? = null
    lateinit var layout: RelativeLayout
    private var listener: IBottomSheetCallback? = null
    private var action: Int = 0
    private lateinit var ivImage: ImageView
    private lateinit var tvNote: TextView
    private lateinit var progress_bar: ProgressBar
    private var currentOrder: Order? = null
    private var productsList = ArrayList<Product>()
    private var rvActiveOrdersData:RecyclerView?=null


    var CurrentOrder: Order
        get() {
            return currentOrder!!
        }
        set(medical) {
            currentOrder = medical
        }
    //endregion

    //region Events
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        view = inflater.inflate(R.layout.bottom_sheet_active_order, container, false) as ViewGroup
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
        dialog.dismiss()
    }

    override fun onBottomSheetClosed(isClosed: Boolean) {
        dialog.dismiss()
    }


    //endregion

    //region Helper Functions
    fun init() {

        ivImage = layout.findViewById(R.id.ivImage)
//        tvNote = layout.findViewById(R.id.tvNote)
        progress_bar = layout.findViewById(R.id.progress_bar)
        rvActiveOrdersData=layout.findViewById(R.id.rvActiveOrdersData)


        if (CurrentOrder != null)
            loadOrderData(CurrentOrder)
    }


    private fun loadOrderData(order: Order) {

        progress_bar.visibility = View.VISIBLE

        productsList=order.items
        var adapter=LoadActiveOrderProductsAdapter(fragmentManager,context!!,productsList)
        rvActiveOrdersData!!.adapter=adapter
    }
    //endregion

}