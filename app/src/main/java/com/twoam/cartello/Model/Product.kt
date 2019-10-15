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
    var category: Category? = null
    var active = false
    var isaddedToFavorite=false
    var description=""


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
