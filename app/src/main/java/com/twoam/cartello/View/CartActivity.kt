package com.twoam.cartello.View

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.twoam.cartello.Model.Cart
import com.twoam.cartello.R
import com.twoam.cartello.Utilities.Adapters.CartAdapter
import com.twoam.cartello.Utilities.Base.BaseDefaultActivity
import com.twoam.cartello.Utilities.General.AppController
import com.twoam.cartello.Utilities.General.IBottomSheetCallback
import kotlinx.android.synthetic.main.activity_my_cart.*
import kotlinx.android.synthetic.main.fragment_payment.*
import java.text.NumberFormat

class CartActivity : BaseDefaultActivity(), View.OnClickListener, IBottomSheetCallback {


    //region Members
    private var listener: IBottomSheetCallback? = null

    //end region

    //region Events
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_cart)


        init()
        prepareCartData()
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.tvNotes -> {
            }
            R.id.btnProceed -> {
                startActivity(Intent(this@CartActivity, CheckoutActivity::class.java))
            }
            R.id.ivBackForgetPassword -> {
                finish()
            }
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        listener?.onBottomSheetSelectedItem(4)
    }

    override fun onBottomSheetClosed(isClosed: Boolean) {

    }

    override fun onBottomSheetSelectedItem(index: Int) {
        if (index == 4) {
            var total = NumberFormat.getInstance().format(Cart.getTotal())
            tvTotalPrice.text = getString(R.string.total_1) + " " + getString(R.string.place_order) + " " + total+ " " + getString(R.string.currency)
        }
    }

    //endregion
    //region Helper Function

    private fun init() {

        tvNotes.setOnClickListener(this)
        btnProceed.setOnClickListener(this)
        ivBackForgetPassword.setOnClickListener(this)

    }

    private fun prepareCartData() {
        var products = Cart.getAll()
        if (products.count() <= 0) {
            tvEmptyData.visibility = View.VISIBLE
            btnProceed.alpha= 0.7F
            btnProceed.isEnabled = false
        } else {
            tvEmptyData.visibility = View.GONE
            btnProceed.alpha= 1F
            btnProceed.isEnabled = true
            tvTotalProduct.text = Cart.getAll().count().toString() + " " + getString(R.string.products)
            tvTotalPrice.text = getString(R.string.total_1) + " " + Cart.getTotalCost()

            var total = NumberFormat.getInstance().format(Cart.getTotal())
            tvTotalPrice.text = getString(R.string.total_1) + " "  + total+ " " + getString(R.string.currency)

            var adapter = CartAdapter(this@CartActivity, products)
            rvCartProducts!!.adapter = adapter
            rvCartProducts.layoutManager = LinearLayoutManager(AppController.getContext(), LinearLayoutManager.VERTICAL, false)

        }

    }
    //endregion

}
