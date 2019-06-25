package com.twoam.cartello.Model

/**
 * Created by Mokhtar on 6/18/2019.
 */
class Area {
    var id: Int = 0
    var name: String = ""
    var city_id: Int = 0
    var active: Boolean = false
    var deactivation_notes: String = ""
    var delivery_fees: Long = 0L

    constructor()

    constructor(id: Int, name: String) {
        this.id = id
        this.name = name
    }

    override fun toString(): String {
        return name
    }
}