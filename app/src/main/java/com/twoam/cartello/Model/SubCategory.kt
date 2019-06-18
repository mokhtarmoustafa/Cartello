package com.twoam.cartello.Model

/**
 * Created by Mokhtar on 6/18/2019.
 */
class SubCategory {
    private var id: Int = 0
    private var name: String = ""
    private var description: String = ""
    private var parent_id: Int = 0
    private var created_at: String = ""
    private var updated_at: String = ""
    private lateinit var options: ArrayList<Options>
    private var max_price: Long  =0
    private var min_price: Long  = 0


}

