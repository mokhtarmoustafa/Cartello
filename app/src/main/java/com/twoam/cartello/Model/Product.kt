package com.twoam.cartello.Model

/**
 * Created by Mokhtar on 6/30/2019.
 */

class Product {
    var id: Int = 0
    var name: String? = null
    var image: String? = null
    var price: Double? = null
    var discount_price: Double? = null
    var amount: Int = 0 //quantity
    var itemImage: Int = 0
    var images: ArrayList<String>? = null
    var description: String? = null
    var is_favourite: Boolean = false
    var similar_products: ArrayList<Product>? = null
    var category: Category? = null
    var details: String? = null
    var active = false

    constructor()

    constructor(id: Int, name: String, image: String, price: Double, discountPrice: Double) {
        this.id = id
        this.name = name
        this.image = image
        this.price = price
        this.discount_price = discountPrice

    }


    constructor(id: Int, quantity: Int, name: String, image: String, price: Double, category: Category, discountPrice: Double) {
        this.id = id
        this.amount = quantity
        this.name = name
        this.image = image
        this.price = price
        this.category = category
        this.discount_price = discountPrice

    }

}
