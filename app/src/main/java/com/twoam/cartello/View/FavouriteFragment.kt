package com.twoam.cartello.View


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.twoam.Networking.INetworkCallBack
import com.twoam.Networking.NetworkManager
import com.twoam.cartello.Model.Product

import com.twoam.cartello.R
import com.twoam.cartello.Utilities.API.ApiResponse
import com.twoam.cartello.Utilities.API.ApiServices
import com.twoam.cartello.Utilities.Adapters.FavouriteAdapter
import com.twoam.cartello.Utilities.Base.BaseFragment
import com.twoam.cartello.Utilities.General.*
import kotlinx.android.synthetic.main.fragment_orders.*


/**
 * A simple [Fragment] subclass.
 */
class FavouriteFragment : BaseFragment(), IProductFavouritesCallback {

    //region Members
    private lateinit var currentView: View
    private var rvFavourites: RecyclerView? = null
    private var tvEmptyDataFavouriteFavourite: TextView? = null
    private var tvTotalFavourites: TextView? = null
    private var adapter: FavouriteAdapter? = null
    //endregion


    //region Events

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        currentView = inflater.inflate(R.layout.fragment_favourite, container, false)

        init()
        getFavourites()
        return currentView
    }

    override fun onAddToFavourite(product: Product?) {
        addToFavourites(product!!)

    }

    override fun onRemoveFromFavourite(product: Product?) {
        removeFromFavourites(product!!)
    }

    //endregion

    //region Helper Functions
    private fun getFavourites() {
        if (NetworkManager().isNetworkAvailable(context!!)) {
            var token = AppConstants.BEARER + AppConstants.CurrentLoginUser.token
            var request = NetworkManager().create(ApiServices::class.java)
            var endPoint = request.getFavourites(token)
            NetworkManager().request(endPoint, object : INetworkCallBack<ApiResponse<ArrayList<Product>>> {
                override fun onFailed(error: String) {
                    showAlertDialouge(error)
                }

                override fun onSuccess(response: ApiResponse<ArrayList<Product>>) {
                    if (response.code == AppConstants.CODE_200) {
                        prepareFavouritesData(response.data!!)
                    } else {
                        showAlertDialouge(getString(R.string.error_network))
                    }
                }
            })
        } else {
            showAlertDialouge(getString(R.string.error_no_internet))
        }
    }

    private fun addToFavourites(product: Product) {
        if (NetworkManager().isNetworkAvailable(context!!)) {
            var token = AppConstants.BEARER + AppConstants.CurrentLoginUser.token
            var request = NetworkManager().create(ApiServices::class.java)
            var endPoint = request.addToFavourite(token, product.id)
            NetworkManager().request(endPoint, object : INetworkCallBack<ApiResponse<Boolean>> {
                override fun onFailed(error: String) {
                    showAlertDialouge(error)
                }

                override fun onSuccess(response: ApiResponse<Boolean>) {
                    if (response.code == AppConstants.CODE_200) {
                        //refresh data
                        adapter?.notifyDataSetChanged()
                    } else {
                        showAlertDialouge(getString(R.string.error_network))
                    }
                }
            })
        } else {
            showAlertDialouge(getString(R.string.error_no_internet))
        }
    }

    private fun removeFromFavourites(product: Product) {
        if (NetworkManager().isNetworkAvailable(context!!)) {
            var token = AppConstants.BEARER + AppConstants.CurrentLoginUser.token
            var request = NetworkManager().create(ApiServices::class.java)
            var endPoint = request.removeFromFavourite(token, product.id)
            NetworkManager().request(endPoint, object : INetworkCallBack<ApiResponse<Boolean>> {
                override fun onFailed(error: String) {
                    showAlertDialouge(error)
                }

                override fun onSuccess(response: ApiResponse<Boolean>) {
                    if (response.code == AppConstants.CODE_200) {
                        //refresh data
                        adapter?.notifyDataSetChanged()
                    } else {
                        showAlertDialouge(getString(R.string.error_network))
                    }
                }
            })
        } else {
            showAlertDialouge(getString(R.string.error_no_internet))
        }
    }

    private fun prepareFavouritesData(products: ArrayList<Product>) {
        if (products.size > 0) {
            tvEmptyDataFavouriteFavourite?.visibility = View.INVISIBLE
            tvTotalFavourites?.text = products.size.toString()
        } else
            tvEmptyDataFavouriteFavourite?.visibility = View.VISIBLE

         adapter = FavouriteAdapter(context!!, products)
        rvFavourites?.adapter = adapter
        rvFavourites?.layoutManager = GridLayoutManager(AppController.getContext(),2, GridLayoutManager.VERTICAL, false)


    }

    private fun init() {
        rvFavourites = currentView.findViewById(R.id.rvFavourites)
        tvEmptyDataFavouriteFavourite = currentView.findViewById(R.id.tvEmptyDataFavouriteFavourite)
        tvTotalFavourites = currentView.findViewById(R.id.tvTotalFavourites)
    }
    //endregion


}
