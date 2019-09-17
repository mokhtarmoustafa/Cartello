package com.twoam.cartello.View


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.twoam.Networking.INetworkCallBack
import com.twoam.Networking.NetworkManager
import com.twoam.cartello.Model.*

import com.twoam.cartello.R
import com.twoam.cartello.Utilities.API.ApiResponse
import com.twoam.cartello.Utilities.API.ApiServices
import com.twoam.cartello.Utilities.Adapters.CheckoutPaymnetTypesAdapter
import com.twoam.cartello.Utilities.Base.BaseFragment
import com.twoam.cartello.Utilities.General.AppConstants
import com.twoam.cartello.Utilities.General.AppController
import kotlinx.android.synthetic.main.activity_checkout.*

import org.json.JSONObject


/**
 * A simple [Fragment] subclass.
 */
class PaymentFragment : BaseFragment() {

    //region Members
    var currentView: View? = null
    var ivPromoCode: ImageView? = null
    var rvPaymentTypes: RecyclerView? = null
    var orderList = ArrayList<Order>()

    var paymnetTypesList = ArrayList<PaymentType>()
    private var btnPlaceOrder: Button? = null
    private var trackingFragment: TrackFragment = TrackFragment()
//    private var  checkoutFragment:CheckoutFragment= CheckoutFragment()
// endregion

    //region Events
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        currentView = inflater.inflate(R.layout.fragment_payment, container, false)

        init()
        getPaymentTypes()
        return currentView
    }


// endregion

    //region Helper Functions
    private fun init() {
        ivPromoCode = currentView?.findViewById(R.id.ivPromoCode)
        rvPaymentTypes = currentView?.findViewById(R.id.rvPaymentTypes)
        btnPlaceOrder = currentView?.findViewById(R.id.btnPlaceOrder)

        (activity as CheckoutActivity).ivPayment.visibility = View.VISIBLE
        (activity as CheckoutActivity).ivPaymentNon.visibility = View.INVISIBLE
        (activity as CheckoutActivity).ivCheckoutNon.visibility = View.VISIBLE
        (activity as CheckoutActivity).ivCheckout.visibility = View.INVISIBLE

        ivPromoCode?.setOnClickListener(View.OnClickListener {
            //todo open bottom sheet
        })


        btnPlaceOrder?.setOnClickListener({
            Toast.makeText(context, "ORDER :" + AppConstants.CurrentSelectedAddress.address.length, Toast.LENGTH_SHORT).show()
            var order = Order()
            order.payment_method = 1

            var itemsList = ArrayList<Items>()
            Cart.getAll().forEach { product ->
                order.items.add(product)
                var item = Items(product.id, product.amount)
                itemsList.add(item)
            }
            val array = arrayOfNulls<Items>(itemsList.size)
            for (i in itemsList.indices) {
                array[i] = itemsList[i]
            }

            createOrder(array)
        })

    }

    private fun createOrder(items: Array<Items?>) {
        if (NetworkManager().isNetworkAvailable(context!!)) {
            var request = NetworkManager().create(ApiServices::class.java)
            var token = AppConstants.BEARER + AppConstants.CurrentLoginUser.token

            val paramObject = JSONObject()
            paramObject.put("payment_method", 1)
            paramObject.put("items", items)
            paramObject.put("address_id", AppConstants.CurrentSelectedAddress.id)

//            var endPoint = request.createOrder(token, 1, items, AppConstants.CurrentSelectedAddress.id)
            var endPoint = request.createOrder(token, 1, items, AppConstants.CurrentSelectedAddress.id)
            NetworkManager().request(endPoint, object : INetworkCallBack<ApiResponse<Order>> {
                override fun onFailed(error: String) {
                    showAlertDialouge(error)
                }

                override fun onSuccess(response: ApiResponse<Order>) {
                    if (response.code == AppConstants.CODE_200) {
                        Cart.emptyCart()
                        fragmentManager?.beginTransaction()?.replace(R.id.layout_container, trackingFragment, "trackingFragment")?.commit()

                    } else {
                        Toast.makeText(AppController.getContext(), response.message, Toast.LENGTH_SHORT).show()
                    }
                }
            })
        } else {
            showAlertDialouge(getString(R.string.error_no_internet))
        }
    }

    private fun getPaymentTypes() {
        var paymentType = PaymentType(1, getString(R.string.cash))

        paymnetTypesList.add(paymentType)

        var adapter = CheckoutPaymnetTypesAdapter(context!!, paymnetTypesList, this)
        rvPaymentTypes!!.adapter = adapter
        rvPaymentTypes!!.layoutManager = LinearLayoutManager(AppController.getContext(), LinearLayoutManager.VERTICAL, false)
    }


    private fun checkPromo() {}
// endregion


}// Required empty public constructor
