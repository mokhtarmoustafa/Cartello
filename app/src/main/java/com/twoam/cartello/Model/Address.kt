package com.twoam.cartello.Model

/**
 * Created by Mokhtar on 6/18/2019.
 */

class Address {

     var id: Int = 0
     var name: String = ""
     var city_id: Int = 0
     var area_id: Int = 0
     var address: String = ""
     var addressType: String = ""
     var apartment: String = ""
     var floor: String = ""
     var landmark: String = ""
     var user_id: Int = 0
     lateinit var addresses: ArrayList<Address>
}