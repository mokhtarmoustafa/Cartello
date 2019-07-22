package com.twoam.cartello.Model

import com.twoam.cartello.R.string.category
import com.twoam.cartello.R.string.name
import com.twoam.cartello.Utilities.DB.PreferenceController
import com.twoam.cartello.Utilities.General.AppConstants
import com.twoam.cartello.Utilities.General.AppController
import com.twoam.cartello.Utilities.General.NotificationCenter

/**
 * Created by Mokhtar on 7/20/2019.
 */
class Cart {
    var shared = Cart()

//    private var products: ArrayList<Product>
//    {
//        didSet
//        {
//            NotificationCenter.post(name: Notification. Name ("CartCount"), object: nil)
//        }
//    }

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

    fun saveToDisk() {
        PreferenceController.getInstance(AppController.getContext()).setCartPref(AppConstants.CART_ITEMS, products)
    }

    fun delete(product: Product) {
        if (products.contains(product)) {
            var index = products.indexOf(product)
            products.removeAt(index)
        }

    }

//    func getQty<T: ProductProtocol>(for product: T) -> Int
//    {
//        if let index = products.index(of: Product(id: product.id, amount: nil, name: "", image: "", price: 0,
// category: Category(id: 0, name: "", description: "", subCategories: nil, image: nil), details: nil, discountPrice: nil, active: true))
//        {
//            return products[index].amount ?? 0
//        }
//        return 0
//    }

    fun <T : ProductProtocol> getQty(product: T): Int {
//        var index = products.indexOf(product)
//        if (var index = products.indexOf( Product(0, null, "", "",  0,
//        category, null,
//      null, true))
//        {
//            return products[index].amount ?? 0
//        }
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
        var total = 0.0
        for (product in products.indices) {
            total = total.plus(products[product].price!!.toDouble())
            //        return products.reduce(0, { $0 + (($1.discountPrice ?? $1.price) * Double($1.quantity ?? 0)) })
        }
        return total
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

class ProductProtocol {

}
