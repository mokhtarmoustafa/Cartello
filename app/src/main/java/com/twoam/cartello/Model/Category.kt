package com.twoam.cartello.Model

/**
 * Created by Mokhtar on 6/18/2019.
 */
class Category {
    private var id: Int = 0
    private var name: String = ""
    private var description: String = ""
    private var parent_id: Int = 0
    private var created_at: String = ""
    private var updated_at: String = ""
    private var order: Int = 0
    private var image: String? = null
    private lateinit var sub_categories: ArrayList<SubCategory>


}