package com.twoam.cartello.Model


/**
 * Created by Mokhtar on 6/18/2019.
 */

class PaymentType {

    var id: Int = 0
    var name: String = ""
    var image: Int = 0

constructor()
    constructor(id: Int, name: String) {
        this.id = id
        this.name = name
    }

}