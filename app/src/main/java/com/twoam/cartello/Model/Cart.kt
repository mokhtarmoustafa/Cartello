package com.twoam.cartello.Model

import com.twoam.cartello.Utilities.DB.PreferenceController
import com.twoam.cartello.Utilities.General.AppConstants
import com.twoam.cartello.Utilities.General.AppController


/**
 * Created by Mokhtar on 7/20/2019.
 */
class Cart {
    var shared = Cart()


    private var products: ArrayList<Product>
        get() {
            return ArrayList()
        }
        set(productsList) {
            products = productsList
        }

    var notes: String? = null

    private fun init() {
        products = PreferenceController.getInstance(AppController.getContext()).getCartPref(AppConstants.CART_ITEMS)!!
    }

    fun emptyCart() {
        PreferenceController.getInstance(AppController.getContext()).clear(AppConstants.CART_ITEMS)
        this.shared = Cart()
    }

    fun addProduct(product: Product) {
        if (product.quantity == 0) {
            deleteProduct(product)
        } else {
            updateProduct(product)
        }
    }

    //static
    fun saveToDisk() {
        PreferenceController.getInstance(AppController.getContext()).setCartPref(AppConstants.CART_ITEMS, products)
    }

    fun delete(product: Product) {
        if (products.contains(product)) {
            var index = products.indexOf(product)
            products.removeAt(index)
        }
    }

    fun getQty(product: Product): Int {
        var index = products.indexOf(product)
        if (index > 0) {
            return products[index].quantity
        }
        return 0
    }

    fun getQuantity(product: Product): Int {
        return product.quantity
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
            value = product.discount_price ?: product.price ?: 0 * product.quantity as Double
        }

        return value
    }


    private fun updateProduct(product: Product) {
// Product exists in cart
        if (products.contains(product)) {
            var index = products.indexOf(product)
            products[index].quantity = product.quantity
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



