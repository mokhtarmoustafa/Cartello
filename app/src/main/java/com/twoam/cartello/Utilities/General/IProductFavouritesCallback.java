package com.twoam.cartello.Utilities.General;


import com.twoam.cartello.Model.Product;

public interface IProductFavouritesCallback {

    void onAddToFavourite(Product product);
 void onRemoveFromFavourite(Product product);


}
