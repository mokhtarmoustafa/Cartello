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
import com.bumptech.glide.Glide.init
import com.twoam.Networking.INetworkCallBack
import com.twoam.Networking.NetworkManager
import com.twoam.cartello.Model.Order
import com.twoam.cartello.Model.Product

import com.twoam.cartello.R
import com.twoam.cartello.Utilities.API.ApiResponse
import com.twoam.cartello.Utilities.API.ApiServices
import com.twoam.cartello.Utilities.Adapters.ActiveOrdersAdapter
import com.twoam.cartello.Utilities.Adapters.InActiveOrdersAdapter
import com.twoam.cartello.Utilities.Base.BaseFragment
import com.twoam.cartello.Utilities.General.AppConstants
import com.twoam.cartello.Utilities.General.AppController
import com.twoam.cartello.Utilities.General.IBottomSheetCallback
import com.twoam.cartello.Utilities.General.IOrderCallback
import kotlinx.android.synthetic.main.fragment_orders.*
import java.util.function.Predicate


class OrdersFragment : BaseFragment() {


    //region Members
    var currentView: View? = null
    var ordersList = ArrayList<Order>()
    var activeOrderList = ArrayList<Order>()
    var inActiveOrderList = ArrayList<Order>()
    var listener: IBottomSheetCallback? = null


    //endregion

    //region Events
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        currentView = inflater.inflate(R.layout.fragment_orders, container, false)

        getOrders()

        return currentView
    }


    //endregion

    //region Helper Functions


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
        var adapter = ActiveOrdersAdapter(childFragmentManager, AppController.getContext(), activeOrdersList)
        rvActiveOrders.adapter = adapter
        rvActiveOrders.layoutManager = LinearLayoutManager(AppController.getContext(), LinearLayoutManager.HORIZONTAL, false)
    }

    private fun getInActiveOrders(inActiveOrdersList: ArrayList<Order>) {
        //adapt the data
        var adapter = InActiveOrdersAdapter(fragmentManager, AppController.getContext(), inActiveOrdersList)
        rvInActiveOrders.adapter = adapter
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

        tvTotalOrder.text = activeOrderList.count().toString()

        if (activeOrderList.count() == 0)
            activeOrderList.add(Order())

        getActiveOrders(activeOrderList)
        getInActiveOrders(inActiveOrderList)
    }

    fun cancelOrder(order: Order) {
        showDialogue()
        if (NetworkManager().isNetworkAvailable(AppController.getContext())) {
            var request = NetworkManager().create(ApiServices::class.java)
            var token = AppConstants.BEARER + AppConstants.CurrentLoginUser.token
            var endPoint = request.cancelOrder(token, order.id)
            NetworkManager().request(endPoint, object : INetworkCallBack<ApiResponse<Order>> {
                override fun onFailed(error: String) {
                    hideDialogue()
                    showAlertDialouge(error)
                }

                override fun onSuccess(response: ApiResponse<Order>) {
                    if (response.code == AppConstants.CODE_200) {

                        activeOrderList.remove(order)
                        var count = activeOrderList.count()
                        tvTotalOrder.text = count.toString()
                        inActiveOrderList.add(order)

                        if (activeOrderList.count() == 0)
                            activeOrderList.add(Order())

                        rvActiveOrders.adapter.notifyDataSetChanged()
                        rvInActiveOrders.adapter.notifyDataSetChanged()
                        hideDialogue()

                    } else {
                        hideDialogue()
                        showAlertDialouge(getString(R.string.error_login_server_error))
                    }
                }
            })
        } else {
            hideDialogue()
            showAlertDialouge(getString(R.string.error_no_internet))
        }
    }
    //endregion


}
