package com.twoam.cartello.Utilities.Interfaces


import com.twoam.cartello.Model.Product

interface IProductFavouritesCallback {

    fun onAddToFavourite(product: Product)

    fun onRemoveFromFavourite(product: Product)

    fun getSimilarProducts(productId:Int)


}
