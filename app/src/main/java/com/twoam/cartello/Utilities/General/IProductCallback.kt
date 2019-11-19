package com.twoam.cartello.Utilities.General


import com.twoam.cartello.Model.Cart
import com.twoam.cartello.Model.Product

interface IProductCallback {

    fun OnIncrementProductQuantity(product: Product)

    fun onDecrementProductQuantity(product: Product)

    private fun addProduct(product: Product): Int {
        var storedProduct = Cart.getAll().find { it.id == product.id }
        if (storedProduct != null && storedProduct!!.id > 0) {
            var amount = Cart.getAll().find { it.id == product.id }?.amount
            Cart.getAll().find { it.id == product.id }?.amount = amount!! + 1
            Cart.addProduct(product)
            Cart.getProductQuantity(product).toString()
            Cart.saveToDisk()

        } else {
            product.amount = 1
            Cart.addProduct(product)
            Cart.getProductQuantity(product).toString()
            Cart.saveToDisk()


        }
        return Cart.getProductQuantity(product)
    }

}
