package com.twoam.cartello.Utilities.Adapters

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.twoam.cartello.Model.Cart
import com.twoam.cartello.Model.Product
import com.twoam.cartello.R
import com.twoam.cartello.Utilities.General.AppConstants
import com.twoam.cartello.Utilities.General.AppController
import com.twoam.cartello.Utilities.General.IBottomSheetCallback
import com.twoam.cartello.View.ProductDetailsActivity

import java.util.ArrayList
import com.bumptech.glide.request.RequestOptions
import com.twoam.Networking.INetworkCallBack
import com.twoam.Networking.NetworkManager
import com.twoam.cartello.Model.Cart.addProduct
import com.twoam.cartello.Utilities.API.ApiResponse
import com.twoam.cartello.Utilities.API.ApiServices
import com.twoam.cartello.Utilities.General.IProductFavouritesCallback
import com.twoam.cartello.View.FavouriteFragment


/**
 * Created by Mokhtar on 6/30/2019.
 */

class FavouriteAdapter(private val context: Context, private val productsList: ArrayList<Product>,
                       private val _favouriteListener: IProductFavouritesCallback,private val _productListener:IBottomSheetCallback)
    : RecyclerView.Adapter<FavouriteAdapter.MyViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var product = Product()
    private var listener: IBottomSheetCallback? = null
    private var favouriteListener: IProductFavouritesCallback? = null
    private var productListener: IBottomSheetCallback? = null



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        favouriteListener=_favouriteListener
        productListener=_productListener

        val view = inflater.inflate(R.layout.favourite_product_layout, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        product = productsList[position]
        val requestOptions = RequestOptions()
        requestOptions.placeholder(R.drawable.no_image)

        Glide.with(context)
                .setDefaultRequestOptions(requestOptions)
                .load(product.image)
                .into(holder.productImage)

        holder.productName.text = product.name
        holder.tvPrice.text = product.price.toString() + " " + AppController.getContext().getString(R.string.currency)
        if (product.discount_price == null) {
            holder.tvDiscountPrice.visibility = View.INVISIBLE
        }
        if (product.discount_price != null) {
            holder.tvPrice.paintFlags = holder.tvPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            holder.tvDiscountPrice.text = product.discount_price.toString() + " " + AppController.getContext().getString(R.string.currency)
        }

        //check if product exist in cart to update its value
        var storedProduct = Cart.getAll().find { it.id == product.id }
        if (storedProduct != null && storedProduct!!.id > 0)
            holder.tvValue.text = storedProduct.amount.toString()
    }

    override fun getItemCount(): Int {
        return productsList.size
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }


    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var cvFavorite: CardView = itemView.findViewById(R.id.cvFavorite)
        var productImage: ImageView = itemView.findViewById(R.id.ivItemImage)
        var productName: TextView = itemView.findViewById(R.id.tvItemName)
        var tvPrice: TextView = itemView.findViewById(R.id.tvOldPrice)
        var tvDiscountPrice: TextView = itemView.findViewById(R.id.tvNewPrice)

        var addItem: ImageView = itemView.findViewById(R.id.addItem)
        var subItem: TextView = itemView.findViewById(R.id.subItem)
        var tvValue: TextView = itemView.findViewById(R.id.tvValue)


        init {


            itemView.setOnClickListener { v ->
                // get position
                val pos = adapterPosition

                // check if item still exists
                if (pos != RecyclerView.NO_POSITION) {
                    product = productsList[pos]
                    AppConstants.CurrentSelectedProduct = product!!
                    productListener?.onBottomSheetSelectedItem(9)
//                    context.startActivity(Intent(context, ProductDetailsActivity::class.java))

                }
            }

            cvFavorite.setOnClickListener {

                val pos = adapterPosition
                product = productsList[pos]
//
//                if (product.isaddedToFavorite) {
//                    addToFavourite(product)
//
//                } else
                    removeFromFavourite(product)

            }

            addItem.setOnClickListener {
                val pos = adapterPosition
                product = productsList[pos]

                addProduct(product)

            }


            subItem.setOnClickListener {
                val pos = adapterPosition
                product = productsList[pos]

                removeProduct(product)
            }


        }

        private fun addToFavourite(product: Product) {

            favouriteListener?.onAddToFavourite(product)

        }

        private fun removeFromFavourite(product: Product) {
            favouriteListener?.onRemoveFromFavourite(product)
        }

        private fun addProduct(product: Product) {
            var storedProduct = Cart.getAll().find { it.id == product.id }
            if (storedProduct != null && storedProduct!!.id > 0) {
                var amount = Cart.getAll().find { it.id == product.id }?.amount
                Cart.getAll().find { it.id == product.id }?.amount = amount!! + 1
                Cart.addProduct(product)
//                tvValue.text = Cart.getProductQuantity(product).toString()
//                Cart.saveToDisk()
//                notifyDataSetChanged()
//                listener?.onBottomSheetSelectedItem(4) //update counter value on main activity
            } else {
                product.amount = 1
                Cart.addProduct(product)
//                tvValue.text = Cart.getProductQuantity(product).toString()
//                Cart.saveToDisk()
//                notifyDataSetChanged()
//                listener?.onBottomSheetSelectedItem(4) //update counter value on main activity

            }
            tvValue.text = Cart.getProductQuantity(product).toString()
            Cart.saveToDisk()
            notifyDataSetChanged()
            listener?.onBottomSheetSelectedItem(4) //update counter value on main activity

        }


        private fun removeProduct(product: Product) {
            var oldProduct = Cart.getAll().find { it.id == product.id }
            if (oldProduct != null && oldProduct!!.id > 0) {
                var oldQuantity = oldProduct?.amount

                if (oldQuantity!! > 0) {
                    oldQuantity -= 1
                    tvValue.text = oldQuantity.toString()
                    oldProduct = Cart.getAll().find { it.id == product.id }
                    oldProduct?.amount = oldQuantity
                    Cart.getAll().find { it.id == product.id }?.amount = oldQuantity
                    Cart.saveToDisk()
                    notifyDataSetChanged()
                    listener?.onBottomSheetSelectedItem(4) //update counter value on main activity
                }
                if (oldQuantity == 0) {
                    Cart.addProduct(oldProduct!!)
                    Cart.saveToDisk()
                    notifyDataSetChanged()
                    listener?.onBottomSheetSelectedItem(4) //update counter value on main activity
                }
            }

        }


    }

}
