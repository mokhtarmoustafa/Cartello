package com.twoam.cartello.Model

/**
 * Created by Mokhtar on 6/18/2019.
 */
class SubCategory {
     var id: Int = 0
     var name: String = ""
     var description: String = ""
     var parent_id: Int = 0
     var created_at: String = ""
     var updated_at: String = ""
     lateinit var options: ArrayList<Options>
     var max_price: Long  =0
     var min_price: Long  = 0


}

