package com.twoam.cartello.View

import android.graphics.Paint
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.bumptech.glide.Glide
import com.twoam.Networking.INetworkCallBack
import com.twoam.Networking.NetworkManager
import com.twoam.cartello.Model.Cart
import com.twoam.cartello.Model.Product
import com.twoam.cartello.Model.ProductDetails
import com.twoam.cartello.R
import com.twoam.cartello.Utilities.API.ApiResponse
import com.twoam.cartello.Utilities.API.ApiServices
import com.twoam.cartello.Utilities.Adapters.ProductAdapter
import com.twoam.cartello.Utilities.Base.BaseDefaultActivity
import com.twoam.cartello.Utilities.General.AppConstants
import com.twoam.cartello.Utilities.General.AppController
import com.twoam.cartello.Utilities.General.IProductFavouritesCallback
import kotlinx.android.synthetic.main.activity_product_details.*

class ProductDetailsActivity : BaseDefaultActivity(), IProductFavouritesCallback {


    //region Members
    var counter = 0
    var productId = 0
    //endregion

    //region Events

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)

        init()

        getProductData(productId)
    }

    override fun onAddToFavourite(product: Product) {

    }

    override fun onRemoveFromFavourite(product: Product) {

    }

    override fun getSimilarProducts(productId: Int) {

    }

    //endregion

    //region Helper Functions

    private fun init() {

        showDialogue()
        productId = AppConstants.CurrentSelectedProduct.id
        ivBackForgetPassword.setOnClickListener { finish() }
        ivFavourite.setOnClickListener {
            if (counter % 2 == 0) //add to favourite
            {
                addToFavourite(productId)
                counter += 1
            } else //remove from favourite
            {

                removeFromFavourite(productId)
                counter = 0
            }
        }
    }

    private fun getProductData(productId: Int) {
        if (NetworkManager().isNetworkAvailable(this)) {
            var token = AppConstants.BEARER + AppConstants.CurrentLoginUser.token
            var request = NetworkManager().create(ApiServices::class.java)
            var endPoint = request.getProductDetails(token, productId)

            NetworkManager().request(endPoint, object : INetworkCallBack<ApiResponse<ProductDetails>> {

                override fun onFailed(error: String) {
                    hideDialogue()
                    showAlertDialouge(error)
                }

                override fun onSuccess(response: ApiResponse<ProductDetails>) {
                    if (response.code == AppConstants.CODE_200) {
                        displayProductData(response.data?.product!!)
                        getSimilarProducts(response.data?.similar_products!!)
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

            if(product.is_favourite)
                ivFavourite.setImageResource(R.drawable.favourite_select)

            if (product.discount_price == null) {
                tvDiscountPrice.visibility = View.INVISIBLE
            }

            if (product.discount_price != null) {
                tvPrice.paintFlags = tvPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                tvDiscountPrice.text = product.discount_price.toString() + " " + AppController.getContext().getString(R.string.currency)
            }
            tvPrice.text=product.price.toString()+ " "+getString(R.string.currency)
            tvProductType.text=product.description
            tvValue.text = Cart.getProductQuantity(product).toString()
        }
    }

   private fun  getSimilarProducts(similarProductList:ArrayList<Product>)
    {
        var adapter=ProductAdapter(this,similarProductList,this)
        rvSimilarProducts.adapter=adapter
        rvSimilarProducts.layoutManager = LinearLayoutManager(AppController.getContext(), LinearLayoutManager.HORIZONTAL, false)
        adapter.notifyDataSetChanged()
    }
    fun addToFavourite(productId: Int) {
        if (NetworkManager().isNetworkAvailable(this)) {
            var token = AppConstants.BEARER + AppConstants.CurrentLoginUser.token
            var request = NetworkManager().create(ApiServices::class.java)
            var endPoint = request.addToFavourite(token, productId)
            NetworkManager().request(endPoint, object : INetworkCallBack<ApiResponse<Boolean>> {
                override fun onFailed(error: String) {
                    hideDialogue()
                    showAlertDialouge(error)
                }

                override fun onSuccess(response: ApiResponse<Boolean>) {
                    if (response.code == AppConstants.CODE_200) {
                        hideDialogue()
                        ivFavourite.setImageResource(R.drawable.favourite_select)
                        counter += 1
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

    fun removeFromFavourite(productId: Int) {
        if (NetworkManager().isNetworkAvailable(this)) {
            var token = AppConstants.BEARER + AppConstants.CurrentLoginUser.token
            var request = NetworkManager().create(ApiServices::class.java)
            var endPoint = request.removeFromFavourite(token, productId)
            NetworkManager().request(endPoint, object : INetworkCallBack<ApiResponse<Boolean>> {
                override fun onFailed(error: String) {
                    hideDialogue()
                    showAlertDialouge(error)
                }

                override fun onSuccess(response: ApiResponse<Boolean>) {
                    if (response.code == AppConstants.CODE_200) {
                        ivFavourite.setImageResource(R.drawable.fav)
                        counter = 0
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


    //endregion


}

