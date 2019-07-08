package com.twoam.cartello.Model

/**
 * Created by Mokhtar on 6/30/2019.
 */

class Products {
    var id: String? = null
    var name: String? = null
    var image: String? = null
    var price: String? = null
    private var totlItemCount = 0
    private var category: Category? = null
    private var sub_categories: ArrayList<SubCategory>? = null

    constructor()
}
