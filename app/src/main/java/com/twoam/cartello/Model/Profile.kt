package com.twoam.cartello.Model

import com.twoam.cartello.Utilities.DB.PreferenceController
import com.twoam.cartello.Utilities.General.AppConstants
import com.twoam.cartello.Utilities.General.AppController


/**
 * Created by Mokhtar on 6/18/2019.
 */

 class Profile  {

    var id : Int=0

    var name : String=""

    var email : String=""

    var phone : Int=0

    var birthdate : String=""

    var token : String=""

    var rate : Int=0

   var addresses= ArrayList<Addresses>()

}