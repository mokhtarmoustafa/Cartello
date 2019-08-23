package com.twoam.cartello.View

import android.os.Bundle
import com.bumptech.glide.Glide
import com.twoam.Networking.INetworkCallBack
import com.twoam.Networking.NetworkManager
import com.twoam.cartello.Model.Cart
import com.twoam.cartello.Model.Product
import com.twoam.cartello.R
import com.twoam.cartello.Utilities.API.ApiResponse
import com.twoam.cartello.Utilities.API.ApiServices
import com.twoam.cartello.Utilities.Base.BaseDefaultActivity
import com.twoam.cartello.Utilities.General.AppConstants
import kotlinx.android.synthetic.main.activity_product_details.*

class ProductDetailsActivity : BaseDefaultActivity() {


    //region Members

    //endregion

    //region Events

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)

        init()
    }
    //endregion

    //region Helper Functions

    private fun init() {

        showDialogue()
        getProductData(AppConstants.CurrentSelectedProduct.id)
    }

    private fun getProductData(productId: Int) {
        if (NetworkManager().isNetworkAvailable(this)) {
            var token = AppConstants.BEARER + AppConstants.CurrentLoginUser.token
            var request = NetworkManager().create(ApiServices::class.java)
            var endPoint = request.getProductDetails(token, productId)
            NetworkManager().request(endPoint, object : INetworkCallBack<ApiResponse<Product>> {
                override fun onFailed(error: String) {
                    hideDialogue()
                    showAlertDialouge(error)
                }

                override fun onSuccess(response: ApiResponse<Product>) {
                    if (response.code == AppConstants.CODE_200) {
                        displayProductData(response.data!!)
                        hideDialogue()
                    } else {
                        hideDialogue()
                        showAlertDialouge(response.message)
                    }
                }
            })
        } else {
            hideDialogue()
            showAlertDialouge(getString(R.string.error_no_internet))
        }
    }

    private fun displayProductData(product: Product) {
        if (product.id > 0) {
            Glide.with(this).load(product.image).into(ivProductBigImage)
            Glide.with(this).load(product.image).into(ivProductImage)
            tvProductName.text = product.name
            tvCategory.text = product.category?.name
            tvValue.text = Cart.getProductQuantity(product).toString()
        }
        //endregion


    }
}
