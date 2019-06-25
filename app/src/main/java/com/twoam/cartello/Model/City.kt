package com.twoam.cartello.Model

/**
 * Created by Mokhtar on 6/18/2019.
 */
class City {
     var id: Int = 0
     var name: String = ""
     var areas: ArrayList<Area>? = null

    constructor()
    constructor(id: Int, name: String) {
        this.id = id
            this.name = name
    }

    override fun toString(): String {
        return name
    }
}