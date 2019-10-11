package com.twoam.cartello.Utilities.General


import com.twoam.cartello.Model.Product

interface IProductFavouritesCallback {

    fun onAddToFavourite(product: Product)

    fun onRemoveFromFavourite(product: Product)


}
