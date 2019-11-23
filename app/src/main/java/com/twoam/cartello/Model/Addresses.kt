package com.twoam.cartello.Model


/**
 * Created by Mokhtar on 6/18/2019.
 */

class Addresses {

    var id: Int = 0
    var user_id: Int = 0
    var name: String = ""
    var address: String = ""
    var apartment: String = ""
    var floor: String = ""
    var landmark: String? = null
    var city_id: Int = 0
    var area_id: Int = 0
    var area = Area()
    var city = City()
    var address_type: Int = 0
}