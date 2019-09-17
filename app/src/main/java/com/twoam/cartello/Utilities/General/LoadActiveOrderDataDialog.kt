package com.twoam.cartello.Utilities.General


import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.*
import com.twoam.cartello.Model.Order
import com.twoam.cartello.Model.Product
import com.twoam.cartello.R
import com.twoam.cartello.Utilities.Adapters.LoadActiveOrderProductsAdapter



class LoadActiveOrderDataDialog : BottomSheetDialogFragment() {



    //region Members
    internal var view: ViewGroup? = null
    lateinit var layout: RelativeLayout
    private var listener: IOrderCallback? = null
    private var action: Int = 0
    private var currentOrder: Order? = null
    private lateinit var tvOrderID: TextView
    private lateinit var ivClose: ImageView
    private lateinit var tvTotalAmountValue: TextView
    private lateinit var tvPaymentTypeValue: TextView
    private lateinit var tvExpectedArrivalData: TextView
    private lateinit var ivPlaced: ImageView
    private lateinit var ivProcessing: ImageView
    private lateinit var ivDelivering: ImageView
    private lateinit var ivCompleted: ImageView
    private lateinit var btnCancelOrder: Button
    private lateinit var progress_bar: ProgressBar
    private var productsList = ArrayList<Product>()
    private lateinit var rvActiveOrdersData: RecyclerView
    lateinit var mLoadingDialog: Dialog
    private var cancelOrderSheet=CloseBottomSheetDialog()

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


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            listener = context  as IOrderCallback

        } catch (e: ClassCastException) {
            throw ClassCastException(
                    context.toString() + " must implement IOrderCallback.")
        }
    }

    //endregion

    //region Helper Functions
    fun init() {

        mLoadingDialog = Dialog(context!!)
        mLoadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mLoadingDialog.setContentView(R.layout.progress_bar)
        mLoadingDialog.window!!.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT)
        mLoadingDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        mLoadingDialog.setCancelable(false)


        tvOrderID = layout.findViewById(R.id.tvOrderID)
        ivClose = layout.findViewById(R.id.ivClose)
        tvTotalAmountValue = layout.findViewById(R.id.tvTotalAmountValue)
        tvPaymentTypeValue = layout.findViewById(R.id.tvPaymentTypeValue)
        tvExpectedArrivalData = layout.findViewById(R.id.tvExpectedArrivalData)
        ivPlaced = layout.findViewById(R.id.ivPlaced)
        ivProcessing = layout.findViewById(R.id.ivProcessing)
        ivDelivering = layout.findViewById(R.id.ivDelivering)
        ivCompleted = layout.findViewById(R.id.ivCompleted)
        btnCancelOrder = layout.findViewById(R.id.btnCancelOrder)
        progress_bar = layout.findViewById(R.id.progress_bar)
        rvActiveOrdersData = layout.findViewById(R.id.rvActiveOrdersData)


        if (CurrentOrder != null)
            loadOrderData(CurrentOrder)

        ivClose?.setOnClickListener({
            this.dismiss()
        })

        btnCancelOrder.setOnClickListener({
            this.dismiss()
            cancelOrderSheet.Action=2
            cancelOrderSheet.CurrentOrder=currentOrder!!
            cancelOrderSheet.show(fragmentManager, "cancel order sheet")


        })
    }


    private fun loadOrderData(order: Order) {

//        progress_bar.visibility = View.VISIBLE

        tvOrderID.text = context!!.getString(R.string.order_id) + " " + order.id
        productsList = order.items
        var adapter = LoadActiveOrderProductsAdapter(fragmentManager, context!!, productsList)
        rvActiveOrdersData!!.adapter = adapter
        rvActiveOrdersData.layoutManager = LinearLayoutManager(AppController.getContext(), LinearLayoutManager.VERTICAL, false)
    }
    //endregion

}