package com.twoam.cartello.View


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.twoam.cartello.Model.Address

import com.twoam.cartello.R
import com.twoam.cartello.Utilities.Adapters.CheckoutAddressAdapter
import com.twoam.cartello.Utilities.Base.BaseFragment
import com.twoam.cartello.Utilities.General.AppConstants
import com.twoam.cartello.Utilities.General.AppController
import kotlinx.android.synthetic.main.activity_checkout.*


/**
 * A simple [Fragment] subclass.
 */
class CheckoutFragment : BaseFragment() {


    //region Members
    private var tvAddAddress: TextView? = null
    private var tvEmptyData: TextView? = null
    private var btnNext: Button? = null
    private var rvCheckout: RecyclerView? = null
    var currentView: View? = null
    var addressList = ArrayList<Address>()
//    var paymentFragment: PaymentFragment = PaymentFragment()
    //endregion

    //region Events
    companion object {

        fun newInstance(): CheckoutFragment {
            return CheckoutFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        currentView = inflater.inflate(R.layout.fragment_checkout, container, false)

        init()
        getUserProfileData()
        return currentView
    }


    //endregion

    //region Helper Functions
    private fun init() {
        tvAddAddress = currentView?.findViewById(R.id.tvAddAddress)
        tvEmptyData = currentView?.findViewById(R.id.tvEmptyData)
        rvCheckout = currentView?.findViewById(R.id.rvCheckout)
        tvAddAddress = currentView?.findViewById(R.id.tvAddAddress)
        btnNext = currentView?.findViewById(R.id.btnNext)

        (activity as CheckoutActivity).ivPayment.visibility = View.INVISIBLE
        (activity as CheckoutActivity).ivPaymentNon.visibility = View.VISIBLE

        (activity as CheckoutActivity).ivCheckoutNon.visibility = View.INVISIBLE
        (activity as CheckoutActivity).ivCheckout.visibility = View.VISIBLE

        tvAddAddress?.setOnClickListener({
            startActivity(Intent(context, CreateAddressActivity::class.java))
        })

        btnNext?.setOnClickListener({
            fragmentManager?.beginTransaction()?.replace(R.id.layout_container, PaymentFragment(), "paymentFragment")?.addToBackStack("paymentFragment")?.commit()

        })
    }


    private fun getUserProfileData() {
        if (AppConstants.CurrentLoginUser.address!!.addresses != null &&
                AppConstants.CurrentLoginUser.address!!.addresses!!.size > 0) {

            addressList = AppConstants.CurrentLoginUser.address!!.addresses!!

            if (addressList.size > 0) {
                tvEmptyData?.visibility = View.INVISIBLE
                var adapter = CheckoutAddressAdapter(context!!, addressList)
                rvCheckout!!.adapter = adapter
                rvCheckout!!.layoutManager = LinearLayoutManager(AppController.getContext(), LinearLayoutManager.VERTICAL, false)
            } else
                tvEmptyData?.visibility = View.VISIBLE
        }

    }
    //endregion


}// Required empty public constructor
