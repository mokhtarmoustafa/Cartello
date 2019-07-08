package com.twoam.cartello.Model

/**
 * Created by Mokhtar on 6/30/2019.
 */

class Product {
    var id: String? = null
    var name: String? = null
    var image: String? = null
    var price: String? = null
    var oldPrice: String? = null
    var newPrice: String? = null
    var itemImage: Int = 0
    private var images: ArrayList<String>? = null
    private var description: String? = null
    private var is_favourite: Boolean = false
    private var similar_products: ArrayList<Product>? = null

    constructor()

    constructor(id: String, name: String, image: String, oldPrice: String, newPrice: String) {
        this.id = id
        this.name = name
        this.image = image
        this.oldPrice = oldPrice
        this.newPrice = newPrice

    }
}
