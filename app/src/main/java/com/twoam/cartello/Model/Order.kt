package com.twoam.cartello.Model

/**
 * Created by Mokhtar on 6/18/2019.
 */
class Order
{
 var id: Int = 0
 var date:String=""
 var payment_method: Int = 0
var state_id: Int = 0
var productsList=ArrayList<Product>()
 var user_id: Int = 0
 var deliverer_id: Int? = null
 var paid_amount: Long =0
 var notes: String = ""
 var rate: Int = 0

 var created_at: String = ""
 var updated_at: String = ""
 var customer_rate: Int? = null
 var address_id: Int = 0
 var feedback: String = ""
}