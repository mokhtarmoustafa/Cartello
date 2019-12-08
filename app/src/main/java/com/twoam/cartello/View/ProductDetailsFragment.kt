package com.twoam.cartello.View


import android.content.Context
import android.graphics.Paint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
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
import com.twoam.cartello.Utilities.Base.BaseFragment
import com.twoam.cartello.Utilities.General.AppConstants
import com.twoam.cartello.Utilities.General.AppController
import com.twoam.cartello.Utilities.Interfaces.IBottomSheetCallback
import com.twoam.cartello.Utilities.Interfaces.IProductFavouritesCallback

/**
 * A simple [Fragment] subclass.
 */
class ProductDetailsFragment : BaseFragment(), IProductFavouritesCallback, IBottomSheetCallback, View.OnClickListener {

    //region Members
    var counter = 0
    var productId = 0
    lateinit var product: Product
    lateinit var currentView: View
    lateinit var progress_bar: ProgressBar
    lateinit var ivBackForgetPassword: ImageView
    lateinit var ivFavourite: ImageView
    lateinit var ivProductBigImage: ImageView
    lateinit var ivProductImage: ImageView
    lateinit var tvProductName: TextView
    lateinit var tvCategory: TextView
    lateinit var tvPrice: TextView
    lateinit var tvDiscountPrice: TextView
    lateinit var subItem: TextView
    lateinit var tvValue: TextView
    lateinit var addItem: ImageView
    lateinit var tvProductType: TextView
    lateinit var rvSimilarProducts: RecyclerView
    private lateinit var listener: IBottomSheetCallback
    var isOpendFromFavoriteView = false
    private lateinit var adapterSimilarPoduct: ProductAdapter


    //endregion

    //region Events


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        currentView = inflater.inflate(R.layout.fragment_product_details, container, false)
        init()

        product = AppConstants.CurrentSelectedProduct

        if (product.id > 0)
            getProductData(productId)

        return currentView
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is IBottomSheetCallback) {
            listener = context
        } else {
            throw ClassCastException(context.toString() + " must implement IBottomSheetCallback.onBottomSheetSelectedItem")
        }
    }


    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            product = AppConstants.CurrentSelectedProduct

            if (product.id > 0)
                getProductData(product.id)
        }

    }

    override fun onResume() {
        super.onResume()
        product = AppConstants.CurrentSelectedProduct

        if (product.id > 0)
            getProductData(product.id)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ivBackForgetPassword -> {
                if (isOpendFromFavoriteView) {
                    listener.onBottomSheetSelectedItem(10)
                } else
                    finish()
            }
            R.id.ivFavourite -> {
                if (product.is_favourite) {
                    removeFromFavourite(product.id)
                    product.is_favourite = false
                } else {
                    addToFavourite(product.id)
                    product.is_favourite = true
                }
            }

            R.id.subItem -> {
                removeProduct(product)
            }
            R.id.addItem -> {

                addProduct(product)
            }

        }
    }

    override fun onAddToFavourite(product: Product) {
        addToFavourites(product)
    }

    override fun onRemoveFromFavourite(product: Product) {
        removeFromFavourites(product)
    }

    override fun getSimilarProducts(productId: Int) {
        getProductData(productId)
    }

    override fun onBottomSheetClosed(isClosed: Boolean) {

    }

    override fun onBottomSheetSelectedItem(index: Int) {
        if(index==4)
            listener.onBottomSheetSelectedItem(4)
        else if (index == 8)
            getProductData(AppConstants.CurrentSelectedProduct.id)


    }

    //endregion

    //region Helper Functions
    private fun init() {
        progress_bar = currentView.findViewById(R.id.progress_bar)
        ivBackForgetPassword = currentView.findViewById(R.id.ivBackForgetPassword)
        ivFavourite = currentView.findViewById(R.id.ivFavourite)
        ivProductBigImage = currentView.findViewById(R.id.ivProductBigImage)
        ivProductImage = currentView.findViewById(R.id.ivProductImage)
        tvProductName = currentView.findViewById(R.id.tvProductName)
        tvCategory = currentView.findViewById(R.id.tvCategory)
        tvPrice = currentView.findViewById(R.id.tvPrice)
        tvDiscountPrice = currentView.findViewById(R.id.tvDiscountPrice)
        subItem = currentView.findViewById(R.id.subItem)
        tvValue = currentView.findViewById(R.id.tvValue)
        addItem = currentView.findViewById(R.id.addItem)
        tvProductType = currentView.findViewById(R.id.tvProductType)
        rvSimilarProducts = currentView.findViewById(R.id.rvSimilarProducts)


        ivBackForgetPassword.setOnClickListener(this)
        ivFavourite.setOnClickListener(this)
        subItem.setOnClickListener(this)
        addItem.setOnClickListener(this)

//        ivBackForgetPassword.setOnClickListener {
//
//            if (isOpendFromFavoriteView) {
//                listener.onBottomSheetSelectedItem(10)
//            } else
//                finish()
//        }
//        ivFavourite.setOnClickListener {
//
//            if (product.is_favourite) {
//                removeFromFavourite(product.id)
//                product.is_favourite = false
//            } else {
//                addToFavourite(product.id)
//                product.is_favourite = true
//            }
//
//        }

    }

    private fun addProduct(product: Product) {
        var amount=0
        var storedProduct = Cart.getAll().find { it.id == product.id }
        if (storedProduct != null && storedProduct!!.id > 0) {
             amount = storedProduct.amount
            Cart.getAll().find { it.id == product.id }?.amount = amount!! + 1
            product.amount=amount+1
        } else {
            product.amount = 1
        }


        Cart.addProduct(product)
        tvValue.text = Cart.getProductQuantity(product).toString()
        Cart.saveToDisk()
        listener?.onBottomSheetSelectedItem(4) //update counter value on main activity

    }

    private fun removeProduct(product: Product) {
        var storedProduct = Cart.getAll().find { it.id == product.id }
        var amount = storedProduct?.amount
        if (storedProduct != null && storedProduct!!.id > 0) {

            if (amount!! > 0) {
                amount -= 1

                if (amount == 0)
                    Cart.removeProduct(storedProduct!!)
                else
                    Cart.getAll().find { it.id == product.id }?.amount = amount.toInt()

            } else if (amount == 0) {
                Cart.removeProduct(storedProduct!!)
                Cart.getAll().find { it.id == product.id }?.amount = amount.toInt()
            }
        } else {
            amount = 0
        }

        tvValue.text = amount?.toInt().toString()
        Cart.saveToDisk()
        listener?.onBottomSheetSelectedItem(4) //update counter value on main activity
    }

    private fun getProductData(productId: Int) {
        showDialogue()
        if (NetworkManager().isNetworkAvailable(context!!)) {
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
                        product = response.data?.product!!
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

            if (product.is_favourite)
                ivFavourite.setImageResource(R.drawable.favourite_select)

            if (product.discount_price == null) {
                tvDiscountPrice.visibility = View.INVISIBLE
            }

            if (product.discount_price != null) {
                tvPrice.paintFlags = tvPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                tvDiscountPrice.text = product.discount_price.toString() + " " + AppController.getContext().getString(R.string.currency)
            }
            tvPrice.text = product.price.toString() + " " + getString(R.string.currency)
            tvProductType.text = product.description
            tvValue.text = Cart.getProductQuantity(product).toString()


            //check if product exist in cart to update its value
            var storedProduct = Cart.getAll().find { it.id == product.id }

            if (storedProduct != null && storedProduct!!.id > 0)
                tvValue.text = storedProduct.amount.toString()
        }
    }

    private fun getSimilarProducts(similarProductList: ArrayList<Product>) {
        adapterSimilarPoduct = ProductAdapter(context!!, similarProductList, this, this)
        rvSimilarProducts.adapter = adapterSimilarPoduct
        rvSimilarProducts.layoutManager = LinearLayoutManager(AppController.getContext(), LinearLayoutManager.HORIZONTAL, false)
        adapterSimilarPoduct.notifyDataSetChanged()
    }

    fun addToFavourite(productId: Int) {
        showDialogue()
        if (NetworkManager().isNetworkAvailable(context!!)) {
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
        showDialogue()
        if (NetworkManager().isNetworkAvailable(context!!)) {
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

    private fun addToFavourites(product: Product) {
        showDialogue()
        if (NetworkManager().isNetworkAvailable(context!!)) {
            var token = AppConstants.BEARER + AppConstants.CurrentLoginUser.token
            var request = NetworkManager().create(ApiServices::class.java)
            var endPoint = request.addToFavourite(token, product.id)
            NetworkManager().request(endPoint, object : INetworkCallBack<ApiResponse<Boolean>> {
                override fun onFailed(error: String) {
                    hideDialogue()
                    showAlertDialouge(error)
                }

                override fun onSuccess(response: ApiResponse<Boolean>) {
                    if (response.code == AppConstants.CODE_200) {
                        //refresh data
                        product.is_favourite = true
                        ivFavourite.setImageResource(R.drawable.favourite_select)
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

    private fun removeFromFavourites(product: Product) {
        showDialogue()
        if (NetworkManager().isNetworkAvailable(context!!)) {
            var token = AppConstants.BEARER + AppConstants.CurrentLoginUser.token
            var request = NetworkManager().create(ApiServices::class.java)
            var endPoint = request.removeFromFavourite(token, product.id)
            NetworkManager().request(endPoint, object : INetworkCallBack<ApiResponse<Boolean>> {
                override fun onFailed(error: String) {
                    hideDialogue()
                    showAlertDialouge(error)
                }

                override fun onSuccess(response: ApiResponse<Boolean>) {
                    if (response.code == AppConstants.CODE_200) {
                        //refresh data
                        ivFavourite.setImageResource(R.drawable.fav)
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

    private fun finish() {
        listener.onBottomSheetSelectedItem(7)
    }
    //endregion
}
