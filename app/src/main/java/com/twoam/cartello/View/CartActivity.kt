package com.twoam.cartello.View

import android.os.Bundle
import com.twoam.cartello.R
import com.twoam.cartello.Utilities.Base.BaseDefaultActivity

class CartActivity : BaseDefaultActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_cart)
    }
}
