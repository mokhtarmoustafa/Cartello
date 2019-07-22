package com.twoam.cartello.Utilities.General

import com.twoam.cartello.Model.Product
import com.twoam.cartello.Model.Products


/**
 * Created by Mokhtar on 7/20/2019.
 */
abstract class CartHelper {

    //open cart with the saved list of products
    abstract fun open(productsList: ArrayList<Product>)

    //close cart and save the list of products
    abstract fun close(productsList: ArrayList<Product>)

    //add product to the list of product
    //check the product is exist
    //true increment the quantity with one
    //else add a new product to the list of products
    abstract fun add(product: Product)

    //check the product is exist in the list
    //if exist decrement the quantity with one
    //if the quantity reach 0 remove the product from products list
    abstract fun sub(product: Product)

    //remove the product from the list
    abstract fun remove(product: Product)

    //calculate all the products cost
    abstract fun calculateTotal(products: ArrayList<Products>)

    //proceed the list of products to a new order and empty the cart
    abstract fun proceed(products: ArrayList<Products>)


}