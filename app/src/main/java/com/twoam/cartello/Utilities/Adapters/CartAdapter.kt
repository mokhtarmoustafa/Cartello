package com.twoam.cartello.Utilities.Adapters

import android.content.Context
import android.content.Intent
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


/**
 * Created by Mokhtar on 6/30/2019.
 */

class CartAdapter(private val context: Context, private val productsList: ArrayList<Product>)
    : RecyclerView.Adapter<CartAdapter.MyViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var product: Product = Product()
    private var listener: IBottomSheetCallback? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartAdapter.MyViewHolder {

        val view = inflater.inflate(R.layout.cart_product_layout, parent, false)
        return MyViewHolder(view)

    }

    override fun onBindViewHolder(holder: CartAdapter.MyViewHolder, position: Int) {
        product = productsList[position]


        Glide.with(context).load(product.image)
                .into(holder.productImage)

        holder.productName.text = product.name
        holder.tvPrice.text = (product.discount_price ?: product.price ?: 0 ).toString() + " " + AppController.getContext().getString(R.string.currency)
        holder.tvValue.text = product.amount.toString()

        if (Cart.getAll().contains(product)) {
            holder.tvValue.text =Cart.getAll().find { it.id == product.id }?.amount.toString()
        }

    }

    override fun getItemCount(): Int {
        return productsList.size
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }


    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var productImage: ImageView = itemView.findViewById(R.id.ivProductImage)
        var productName: TextView = itemView.findViewById(R.id.tvProductName)
        var tvPrice: TextView = itemView.findViewById(R.id.tvPrice)
        var ivDelete: ImageView = itemView.findViewById(R.id.ivDelete)
        var addItem: ImageView = itemView.findViewById(R.id.addItem)
        var subItem: TextView = itemView.findViewById(R.id.subItem)
        var tvValue: TextView = itemView.findViewById(R.id.tvValue)


        init {


            productImage.setOnClickListener({
                val pos = adapterPosition
                product = productsList[pos]
                AppConstants.CurrentSelectedProduct = product
                context.startActivity(Intent(context, ProductDetailsActivity::class.java))

            })


            ivDelete.setOnClickListener({
                val pos = adapterPosition
                product = productsList[pos]
                Cart.delete(product)
                Cart.saveToDisk()
                notifyDataSetChanged()
            })


            addItem.setOnClickListener({
                val pos = adapterPosition
                product = productsList[pos]
                addProduct(product)
            })


            subItem.setOnClickListener({
                val pos = adapterPosition
                product = productsList[pos]
                removeProduct(product)
            })

        }


        private fun addProduct(product: Product) {
            if (Cart.getAll().contains(product)) {
                var amount=Cart.getAll().find { it.id==product.id }?.amount
                Cart.getAll().find { it.id==product.id }?.amount= amount!! +1
                Cart.addProduct(product)
                tvValue.text = Cart.getProductQuantity(product).toString()
                Cart.saveToDisk()
                notifyDataSetChanged()
                listener?.onBottomSheetSelectedItem(4) //update counter value on main activity

            } else {
                product.amount = 1
                Cart.addProduct(product)
                tvValue.text = Cart.getProductQuantity(product).toString()
                Cart.saveToDisk()
                notifyDataSetChanged()
                listener?.onBottomSheetSelectedItem(4) //update counter value on main activity

            }

        }

        private fun removeProduct(product: Product) {
            if (Cart.getAll().contains(product)) {
                var oldProduct = Cart.getAll().find { it.id == product.id }
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
