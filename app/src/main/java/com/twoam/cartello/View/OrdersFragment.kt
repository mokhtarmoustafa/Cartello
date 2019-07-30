package com.twoam.cartello.View

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.twoam.Networking.INetworkCallBack
import com.twoam.Networking.NetworkManager
import com.twoam.cartello.Model.Order

import com.twoam.cartello.R
import com.twoam.cartello.Utilities.API.ApiResponse
import com.twoam.cartello.Utilities.API.ApiServices
import com.twoam.cartello.Utilities.Adapters.ActiveOrdersAdapter
import com.twoam.cartello.Utilities.Adapters.InActiveOrdersAdapter
import com.twoam.cartello.Utilities.Base.BaseFragment
import com.twoam.cartello.Utilities.General.AppConstants
import com.twoam.cartello.Utilities.General.AppController
import kotlinx.android.synthetic.main.fragment_orders.*


class OrdersFragment : BaseFragment() {


    //region Members
    var currentView: View? = null
    var ordersList = ArrayList<Order>()
    var activeOrderList = ArrayList<Order>()
    var inActiveOrderList = ArrayList<Order>()


    //endregion

    //region Events
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        currentView = inflater.inflate(R.layout.fragment_orders, container, false)

        init()

        getOrders()

        return currentView
    }
    //endregion

    //region Helper Functions


    private fun init() {

    }

    private fun getOrders() {

        if (NetworkManager().isNetworkAvailable(context!!)) {
            var request = NetworkManager().create(ApiServices::class.java)
            var token = AppConstants.BEARER + AppConstants.CurrentLoginUser.token
            var endPoint = request.getOrders(token)
            NetworkManager().request(endPoint, object : INetworkCallBack<ApiResponse<ArrayList<Order>>> {
                override fun onFailed(error: String) {
                    hideDialogue()
                    showAlertDialouge(error)
                }

                override fun onSuccess(response: ApiResponse<ArrayList<Order>>) {
                    if (response.code == AppConstants.CODE_200) {

                        ordersList = response.data!!
                        prepareOrderData(ordersList)
                        hideDialogue()
                    } else {
                        hideDialogue()
                        showAlertDialouge(response.message)
                    }
                }
            })
        } else {
            showAlertDialouge(getString(R.string.error_no_internet))
        }
    }

    private fun getActiveOrders(activeOrdersList: ArrayList<Order>) {
        //adapt the data
        var adapter=ActiveOrdersAdapter(fragmentManager,AppController.getContext(),activeOrdersList)
        rvActiveOrders.adapter=adapter
        rvActiveOrders.layoutManager = LinearLayoutManager(AppController.getContext(), LinearLayoutManager.HORIZONTAL, false)
    }

    private fun getInActiveOrders(inActiveOrdersList: ArrayList<Order>) {
        //adapt the data
        var adapter= InActiveOrdersAdapter(fragmentManager,AppController.getContext(),inActiveOrdersList)
        rvInActiveOrders.adapter=adapter
        rvInActiveOrders.layoutManager = LinearLayoutManager(AppController.getContext(), LinearLayoutManager.VERTICAL, false)
    }

    private fun prepareOrderData(orderList: ArrayList<Order>) {
        for (order in orderList) {
            if (order.active) {
                activeOrderList.add(order)
            } else {
                inActiveOrderList.add(order)
            }
        }

        tvTotalOrder.text = orderList.count().toString()

        getActiveOrders(activeOrderList)
        getInActiveOrders(inActiveOrderList)
    }

    //endregion


}
