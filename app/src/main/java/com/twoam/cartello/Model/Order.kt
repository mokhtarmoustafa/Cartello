package com.twoam.cartello.Model

/**
 * Created by Mokhtar on 6/18/2019.
 */
class Order
{
private var id: Int = 0
private var user_id: Int = 0
private var deliverer_id: Int? = null
private var state_id: Int = 0
private var paid_amount: Long =0
private var notes: String = ""
private var rate: Int = 0
private var payment_method: Int = 0
private var created_at: String = ""
private var updated_at: String = ""
private var customer_rate: Int? = null
private var address_id: Int = 0
private var feedback: String = ""
}