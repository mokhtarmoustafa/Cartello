package com.twoam.cartello.Model

/**
 * Created by Mokhtar on 6/18/2019.
 */
class Order {

    var id: Int = 0
    var date: String = ""
    var payment_method: Int = 0
    var state_id: Int = 0
    var items = ArrayList<Product>()
    var states = ArrayList<States>()
    var rate: String? = ""
    var total: Double=0.0
    var item_total:Double=0.0
    var delivery_fees:Double=0.0
    var discount:Double=0.0
    var active = false
    var scheduled_at: String?=null
    var reorder_count = 0
    var created_at: String = ""
    var paid_amount: Long = 0
    var notes: String ?=null

    constructor()
//    var user_id: Int = 0
//    var deliverer_id: Int? = null

//    var rate: Int? = null
//    var total: Double = 0.0


//    var schedule: String? = null


//    var updated_at: String = ""
//    var customer_rate: Int? = null
//    var address_id: Int = 0
//    var feedback: String = ""


}