package com.twoam.cartello.Utilities.Interfaces;


import com.twoam.cartello.Model.Order;

public interface IOrderCallback {

    void onOrderCancelled(boolean isCanceled,Order order);


}
