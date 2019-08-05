package com.twoam.cartello.View


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.bumptech.glide.Glide
import com.twoam.cartello.Model.Product
import com.twoam.cartello.R
import com.twoam.cartello.Utilities.Base.BaseFragment
import com.twoam.cartello.Utilities.DB.PreferenceController
import com.twoam.cartello.Utilities.General.AppConstants


class ProductDetailsFragment : BaseFragment() {


    //region Members

    var currentView: View? = null
    private var ivProductBigImage: ImageView? = null
    private var ivProductImage: ImageView? = null
    private var tvProductName: TextView? = null
    private var tvPrice: TextView? = null
    private var tvCategory: TextView? = null
    private var progress_bar: ProgressBar? = null
    private var addItem: ImageView? = null
    private var tvValue: TextView? = null
    private var subItem: TextView? = null
    private var ivFavourite: ImageView? = null
    var counter = 0
    var productList = ArrayList<Product>()
    var isFavourite = false

    //end region

    //region Events

    //end region

    //region Helper Functions

    private fun init() {
        tvProductName = currentView?.findViewById(R.id.tvProductName)
        tvCategory = currentView?.findViewById(R.id.tvCategory)
        tvPrice = currentView?.findViewById(R.id.tvPrice)
        ivProductBigImage = currentView?.findViewById(R.id.ivProductBigImage)
        ivProductImage = currentView?.findViewById(R.id.ivProductImage)
        progress_bar = currentView?.findViewById(R.id.progress_bar)
        addItem = currentView?.findViewById(R.id.addItem)
        tvValue = currentView?.findViewById(R.id.tvValue)
        subItem = currentView?.findViewById(R.id.subItem)
        ivFavourite = currentView?.findViewById(R.id.ivFavourite)

        addItem?.setOnClickListener({
            tvValue?.text = (counter + 1).toString()
        })

        subItem?.setOnClickListener({
            if (counter >= 0)
                tvValue?.text = (counter - 1).toString()
            else
                tvValue?.text = "0"
        })

        ivFavourite?.setOnClickListener({
            if (!isFavourite) {
                ivFavourite?.setImageResource(R.drawable.favourite_select)
                addToFavourite(AppConstants.CurrentSelectedProduct)
                isFavourite=true
            }

            else if(isFavourite)
            {
                ivFavourite?.setImageResource(R.drawable.favourite_non_select)
                removeFromFavourite(AppConstants.CurrentSelectedProduct)
                isFavourite=false
            }
        })
    }

    private fun getProductData(product: Product) {

        progress_bar?.visibility = View.VISIBLE
        if (product?.id > 0) {
            tvProductName?.text = product.name
            tvCategory?.text = product.category?.name
            tvPrice?.text = product.price.toString() + " " + getString(R.string.currency)
            Glide.with(context!!)
                    .load(product.image)
                    .into(ivProductBigImage!!)

            Glide.with(context!!)
                    .load(product.image)
                    .into(ivProductImage!!)


        }
        progress_bar?.visibility = View.GONE
    }


    private fun addToFavourite(product: Product) {
        productList = PreferenceController.instance?.getFavouriteProductsPref(AppConstants.FAVOURITEPRODUCTS)!!
        productList.add(product)
        PreferenceController.instance?.setFavouriteProductsPref(AppConstants.FAVOURITEPRODUCTS, productList)
    }

    private fun removeFromFavourite(product: Product) {
        productList = PreferenceController.instance?.getFavouriteProductsPref(AppConstants.FAVOURITEPRODUCTS)!!
        if (productList.contains(product)) {
            productList.remove(product)
            PreferenceController.instance?.setFavouriteProductsPref(AppConstants.FAVOURITEPRODUCTS, productList)
        }
    }

    //end region
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        currentView = inflater.inflate(R.layout.fragment_product_details, container, false)

        init()
        getProductData(AppConstants.CurrentSelectedProduct)
        return currentView
    }


}// Required empty public constructor
