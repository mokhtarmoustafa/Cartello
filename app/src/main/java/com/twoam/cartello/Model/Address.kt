package com.twoam.cartello.Model


/**
 * Created by Mokhtar on 6/18/2019.
 */

 class Address  {

     var id: Int = 0
     var name: String = ""
     var city_id: Int = 0
     var area_id: Int = 0
     var address: String = ""
     var apartment: String = ""
     var floor: String = ""
     var landmark: String = ""
     var user_id: Int = 0
    var deleted_at:String=""
    var formatted_address:String=""
    var area=Area()
    var city=City()
      var addresses= ArrayList<Address>()
}