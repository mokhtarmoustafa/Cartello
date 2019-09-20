package com.twoam.cartello.View


import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.twoam.Networking.INetworkCallBack
import com.twoam.Networking.NetworkManager
import com.twoam.cartello.Model.Cart
import com.twoam.cartello.Model.Product
import com.twoam.cartello.R
import com.twoam.cartello.Utilities.API.ApiResponse
import com.twoam.cartello.Utilities.API.ApiServices
import com.twoam.cartello.Utilities.Adapters.ProductAdapter
import com.twoam.cartello.Utilities.Adapters.SearchResultAdapter
import com.twoam.cartello.Utilities.Base.BaseDefaultActivity
import com.twoam.cartello.Utilities.General.AppConstants
import com.twoam.cartello.Utilities.General.AppController
import com.twoam.cartello.Utilities.General.FilterBottomSheetDialog
import com.twoam.cartello.Utilities.General.IBottomSheetCallback
import kotlinx.android.synthetic.main.activity_search_result.*
import java.util.*

class SearchResultActivity : BaseDefaultActivity(), View.OnClickListener, IBottomSheetCallback {

    //region Members
    var searchList = ArrayList<Product>()
    val TAG = SearchResultActivity::class.java!!.simpleName
    var bottomSheetPrice = FilterBottomSheetDialog()
    //endregion

    //region Events
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_result)

        init()

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnPrice -> {
                //show options
                if (NetworkManager().isNetworkAvailable(this@SearchResultActivity))
                    bottomSheetPrice.show(supportFragmentManager, TAG)
                else
                    showAlertDialouge(getString(R.string.error_no_internet))
            }

            R.id.btnAsc -> {
                //arrange adapter data and notify change
                if (NetworkManager().isNetworkAvailable(this@SearchResultActivity))
                    filterAsc()
                else
                    showAlertDialouge(getString(R.string.error_no_internet))
            }

            R.id.btnDes -> {
                //arrange adapter data and notify change
                if (NetworkManager().isNetworkAvailable(this@SearchResultActivity))
                    filterDes()
                else
                    showAlertDialouge(getString(R.string.error_no_internet))
            }

            R.id.ivCart, R.id.tvCartCounter -> {
                var intent = Intent(this@SearchResultActivity, CartActivity::class.java)
                startActivityForResult(intent, 100)
                finish()
            }

            R.id.ivSearch ,R.id.rlBack -> {
                finish()
            }

        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onBottomSheetClosed(isClosed: Boolean) {

    }

    override fun onBottomSheetSelectedItem(index: Int) {
        when (index) {
            1 -> { //Price filterHighToLow
                filterByPriceLowToHigh()
            }
            2 -> { // Price filterLowToHigh
                filterByPriceLowToHigh()
            }
        }
    }

    //endregion

    //region Helper Functions

    fun getSearchData(searchValue: String) {
        if (NetworkManager().isNetworkAvailable(this@SearchResultActivity)) {
            var request = NetworkManager().create(ApiServices::class.java)
            var token = AppConstants.BEARER + AppConstants.CurrentLoginUser.token
            var endPoint = request.searchProducts(token, searchValue)
            NetworkManager().request(endPoint, object : INetworkCallBack<ApiResponse<ArrayList<Product>>> {
                override fun onFailed(error: String) {
                    hideDialogue()
                    showAlertDialouge(error)
                }

                override fun onSuccess(response: ApiResponse<ArrayList<Product>>) {
                    if (response.code == AppConstants.CODE_200) {
                        searchList = response.data!!
                        tvTotal.text = searchList.size.toString()
                        prepareProductsResultData(searchList!!)
                        hideDialogue()
                    } else {
                        hideDialogue()
                        showAlertDialouge(getString(R.string.error_network))
                    }
                }
            })
        } else {
            hideDialogue()
            showAlertDialouge(getString(R.string.error_no_internet))
        }
    }

    private fun init() {
        if (intent.hasExtra(AppConstants.SEARCH_DATA)) {
            var searchValue = intent.getStringExtra(AppConstants.SEARCH_DATA)
            tvTitle.text = searchValue
           tvCartCounter.text = Cart.getAll().count().toString()
            showDialogue()
            getSearchData(searchValue)
        }

    }

    private fun prepareProductsResultData(searchList: ArrayList<Product>) {

        var adapter = SearchResultAdapter(this@SearchResultActivity, searchList)
        rvSearchResult.adapter = adapter
        rvSearchResult.layoutManager = GridLayoutManager(this@SearchResultActivity, 2)

    }


    private fun filterAsc() {
        if (NetworkManager().isNetworkAvailable(this@SearchResultActivity)) {
            var request = NetworkManager().create(ApiServices::class.java)
            var token = AppConstants.BEARER + AppConstants.CurrentLoginUser.token
            var endPoint = request.filterProducts(token, "")
            NetworkManager().request(endPoint, object : INetworkCallBack<ApiResponse<ArrayList<Product>>> {
                override fun onFailed(error: String) {
                    showAlertDialouge(error)
                }

                override fun onSuccess(response: ApiResponse<ArrayList<Product>>) {
                    if (response.code == AppConstants.CODE_200) {
                        searchList = response.data!!
                        tvTotal.text = searchList.size.toString()
                        prepareProductsResultData(searchList!!)
                    } else {
                        showAlertDialouge(getString(R.string.error_network))
                    }
                }
            })
        } else {
            showAlertDialouge(getString(R.string.error_no_internet))
        }
    }

    private fun filterDes() {
        if (NetworkManager().isNetworkAvailable(this@SearchResultActivity)) {
            var request = NetworkManager().create(ApiServices::class.java)
            var token = AppConstants.BEARER + AppConstants.CurrentLoginUser.token
            var endPoint = request.filterProducts(token, "")
            NetworkManager().request(endPoint, object : INetworkCallBack<ApiResponse<ArrayList<Product>>> {
                override fun onFailed(error: String) {
                    showAlertDialouge(error)
                }

                override fun onSuccess(response: ApiResponse<ArrayList<Product>>) {
                    if (response.code == AppConstants.CODE_200) {
                        searchList = response.data!!
                        tvTotal.text = searchList.size.toString()
                        prepareProductsResultData(searchList!!)
                    } else {
                        showAlertDialouge(getString(R.string.error_network))
                    }
                }
            })
        } else {
            showAlertDialouge(getString(R.string.error_no_internet))
        }
    }

    private fun filterByPriceHighToLow() {
        if (NetworkManager().isNetworkAvailable(this@SearchResultActivity)) {
            var request = NetworkManager().create(ApiServices::class.java)
            var token = AppConstants.BEARER + AppConstants.CurrentLoginUser.token
            var endPoint = request.filterProducts(token, "")
            NetworkManager().request(endPoint, object : INetworkCallBack<ApiResponse<ArrayList<Product>>> {
                override fun onFailed(error: String) {
                    showAlertDialouge(error)
                }

                override fun onSuccess(response: ApiResponse<ArrayList<Product>>) {
                    if (response.code == AppConstants.CODE_200) {
                        searchList = response.data!!
                        tvTotal.text = searchList.size.toString()
                        prepareProductsResultData(searchList!!)
                    } else {
                        showAlertDialouge(getString(R.string.error_network))
                    }
                }
            })
        } else {
            showAlertDialouge(getString(R.string.error_no_internet))
        }
    }

    private fun filterByPriceLowToHigh() {
        if (NetworkManager().isNetworkAvailable(this@SearchResultActivity)) {
            var request = NetworkManager().create(ApiServices::class.java)
            var token = AppConstants.BEARER + AppConstants.CurrentLoginUser.token
            var endPoint = request.filterProducts(token, "")
            NetworkManager().request(endPoint, object : INetworkCallBack<ApiResponse<ArrayList<Product>>> {
                override fun onFailed(error: String) {
                    showAlertDialouge(error)
                }

                override fun onSuccess(response: ApiResponse<ArrayList<Product>>) {
                    if (response.code == AppConstants.CODE_200) {
                        searchList = response.data!!
                        tvTotal.text = searchList.size.toString()
                        prepareProductsResultData(searchList!!)
                    } else {
                        showAlertDialouge(getString(R.string.error_network))
                    }
                }
            })
        } else {
            showAlertDialouge(getString(R.string.error_no_internet))
        }
    }
    //endregion

}
