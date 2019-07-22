package com.twoam.cartello.Model

/**
 * Created by Mokhtar on 6/18/2019.
 */
class Category {
    var id: Int = 0
    var name: String = ""
    var description: String = ""
    var parent_id: Int = 0
    var created_at: String = ""
    var updated_at: String = ""
    var order: Int = 0
    var image: String? = null
    lateinit var sub_categories: ArrayList<SubCategory>

    //Category(id: 0, name: "", description: "", subCategories: nil, image: nil)
    constructor(id: Int, name: String, description: String, subCategories: ArrayList<SubCategory>, image: String) {
        this.id = id
        this.name = name
        this.description = description
        this.sub_categories = subCategories
        this.image = image
    }

}