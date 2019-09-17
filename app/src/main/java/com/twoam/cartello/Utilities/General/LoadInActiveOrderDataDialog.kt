package com.twoam.cartello.Utilities.General


import android.content.Context
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.*
import com.twoam.cartello.Model.Cart
import com.twoam.cartello.Model.Order
import com.twoam.cartello.Model.Product
import com.twoam.cartello.R
import com.twoam.cartello.Utilities.Adapters.LoadInActiveOrderProductsAdapter


class LoadInActiveOrderDataDialog : BottomSheetDialogFragment(), IBottomSheetCallback {


    //region Members
    internal var view: ViewGroup? = null
    lateinit var layout: RelativeLayout
    private var listener: IBottomSheetCallback? = null
    private var action: Int = 0
    private var currentOrder: Order? = null
    private var productsList = ArrayList<Product>()
    private var rvOrders: RecyclerView? = null
    private var tvTotal: TextView? = null
    private var ivClose:ImageView?=null
    private var btnAddToCart: Button? = null


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
        view = inflater.inflate(R.layout.bottom_sheet_inactive_order, container, false) as ViewGroup
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
        rvOrders = layout.findViewById(R.id.rvOrders)
        tvTotal = layout.findViewById(R.id.tvTotal)
        ivClose=layout.findViewById(R.id.ivClose)
        btnAddToCart = layout.findViewById(R.id.btnAddToCart)

        btnAddToCart!!.setOnClickListener({
            addProductsToCart(CurrentOrder.items)
        })
        ivClose?.setOnClickListener({
            this.dismiss()
        })


        if (CurrentOrder != null)
            loadOrderData(CurrentOrder)
    }

    private fun addProductsToCart(products: ArrayList<Product>) {

        products.forEach { product ->
            addProduct(product)
        }
        Cart.saveToDisk()
        Toast.makeText(context, getString(R.string.added_to_cart), Toast.LENGTH_SHORT).show()
        listener?.onBottomSheetSelectedItem(4) //to update the cart counter on main view
        this.dismiss()

    }

    private fun addProduct(product: Product) {
        var storedProduct = Cart.getAll().find { it.id == product.id }
        if (storedProduct != null && storedProduct!!.id > 0) {
            var amount = Cart.getAll().find { it.id == product.id }?.amount
            Cart.getAll().find { it.id == product.id }?.amount =
                    amount!! + product.amount!!
        } else {
            product.amount = 1
            Cart.addProduct(product)
        }

    }


    private fun loadOrderData(order: Order) {

        productsList = order.items
        var adapter = LoadInActiveOrderProductsAdapter(fragmentManager, context!!, productsList)
        rvOrders!!.adapter = adapter
        rvOrders!!.layoutManager = LinearLayoutManager(AppController.getContext(), LinearLayoutManager.VERTICAL, false)

        var value = getProductsTotalCost()
        tvTotal!!.text = getString(R.string.total_1) + " " + value
    }

    private fun getProductsTotalCost(): Double {
        var value = 0.0
        productsList.forEach { product: Product ->
            value += if (product.discount_price == null) {
                product.price!! * product.amount
            } else {
                product.discount_price!! * product.amount
            }

        }
        return value
    }


    //endregion

}