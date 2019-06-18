package com.twoam.cartello.Model

/**
 * Created by Mokhtar on 6/18/2019.
 */

class Address {

    private var id: Int = 0
    private var name: String = ""
    private var city_id: Int = 0
    private var area_id: Int = 0
    private var address: String = ""
    private var apartment: String = ""
    private var floor: String = ""
    private var landmark: String = ""
    private var user_id: Int = 0
    private lateinit var addresses: ArrayList<Address>
}