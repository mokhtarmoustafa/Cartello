package com.twoam.cartello.Model


/**
 * Created by Mokhtar on 6/18/2019.
 */
class User {

    var id: Int = 0
    var hasdAddress: Boolean = false
    var name: String = ""
    var email: String = ""
    var phone: String = ""
    var birthdate: String = ""
    var token: String = ""
    var rating: String = ""
    var created_at: String = ""
    var updated_at: String = ""
    lateinit var addresses: ArrayList<Address>

    constructor()


}