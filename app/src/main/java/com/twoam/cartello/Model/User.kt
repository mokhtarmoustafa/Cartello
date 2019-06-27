package com.twoam.cartello.Model

import com.google.android.gms.auth.api.signin.GoogleSignInAccount


/**
 * Created by Mokhtar on 6/18/2019.
 */
class User {

    var id: String = ""
    var hasdAddress: Boolean = false
    var name: String = ""
    var email: String = ""
    var phone: String = ""
    var birthdate: String = ""
    var token: String = ""
    var rating: String = ""
    var created_at: String = ""
    var updated_at: String = ""
    var addresses: ArrayList<Address>? = null
    var socialType: Int? = null
    var fullImagePath: String? = null


    constructor()

    constructor(account: GoogleSignInAccount)
    {
        id = account.id!!
        name = account.displayName.toString()
        email = account.email.toString()
//        picture = account.getPhotoUrl()!!

    }

    companion object {
        val socialType_Facebook: Int = 1
        val socialType_Google: Int = 2
    }

}