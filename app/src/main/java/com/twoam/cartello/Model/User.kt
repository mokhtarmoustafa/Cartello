package com.twoam.cartello.Model


/**
 * Created by Mokhtar on 6/18/2019.
 */
class User {

    private var id: Int = 0
    private var name: String = ""
    private var email: String = ""
    private var phone: String = ""
    private var birthdate: String = ""
    private var token: String = ""
    private var rating: String = ""
    private var created_at: String = ""
    private var updated_at: String = ""
    private lateinit var addresses: ArrayList<Address>

}