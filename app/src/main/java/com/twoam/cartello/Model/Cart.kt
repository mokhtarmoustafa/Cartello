package com.twoam.cartello.Model

import android.os.Build
import com.twoam.cartello.Utilities.DB.PreferenceController
import com.twoam.cartello.Utilities.General.AppConstants
import com.twoam.cartello.Utilities.General.AppController

/**
 * Created by Mokhtar on 8/5/2019.
 */
object Cart {

    //    var shared = Cart()
    var productsList = ArrayList<Product>()

    private var products: ArrayList<Product>
        get() {
            return productsList
        }
        set(products) {
            productsList = products
        }


    fun init() {
        try {
            products = PreferenceController.getInstance(AppController.getContext()).getCartPref(AppConstants.CART_ITEMS)!!
        } catch (ex: Exception) {
            products = ArrayList()

        }


    }

    fun emptyCart() {
        PreferenceController.getInstance(AppController.getContext()).clear(AppConstants.CART_ITEMS)
//        this.shared = Cart()
        products.clear()
    }

    fun addProduct(product: Product) {
        if (product.amount == 0) {
            deleteProduct(product)
        } else {
            updateProduct(product)
        }
    }

    fun removeProduct(product: Product) {
        with(products.iterator()) {
            forEach {
                if (it.id == product.id) {
                    // do some stuff with it
                    remove()
                }
            }
        }

//        products.forEach {
//            if(it.id==product.id)
//            {
//                products.remove(product)
//            }
//        }
    }

    //static
    fun saveToDisk() {
        PreferenceController.getInstance(AppController.getContext()).setCartPref(AppConstants.CART_ITEMS, products)
    }

    fun delete(product: Product) {

        with(products.iterator()) {
            forEach {
                if (it.id == product.id) {
                    // do some stuff with it
                    remove()
                }
            }
        }
    }

    fun getQty(product: Product): Int {
        var index = products.indexOf(product)
        if (index > 0) {
            return products[index].amount
        }
        return 0
    }

    fun getProductQuantity(product: Product): Int {
        return product.amount
    }

    fun getTotalItems(): Int {
        return products.count()
    }

    fun getAll(): ArrayList<Product> {
        return products
    }

    fun getTotalCost(): String {
        return (getTotal().toString()) + " " + "LE"
    }

    fun getTotal(): Double {
        var value = 0.0
        products.forEach { product: Product ->
            value += if (product.discount_price == null) {
                product.price!! * product.amount
            } else {
                product.discount_price!! * product.amount
            }

        }

        return value
    }

    private fun updateProduct(product: Product) {
// Product exists in cart
        if (products.contains(product)) {
            var index = products.indexOf(product)
            products[index].amount = product.amount
        } else {
            // Adding product for the first time to the cart
            products.add(product)
        }
    }

    private fun deleteProduct(product: Product) {
        if (products.contains(product)) {
            var index = products.indexOf(product)
            products.removeAt(index)
        }
    }


}