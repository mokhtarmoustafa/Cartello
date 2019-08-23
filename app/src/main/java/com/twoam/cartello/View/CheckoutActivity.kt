package com.twoam.cartello.View

import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.view.View
import bolts.AppLinkNavigation.navigate
import com.twoam.cartello.R
import com.twoam.cartello.Utilities.Base.BaseDefaultActivity
import com.twoam.cartello.Utilities.Base.BaseFragment
import com.twoam.cartello.Utilities.General.AppConstants
import kotlinx.android.synthetic.main.activity_checkout.*

class CheckoutActivity : BaseDefaultActivity() {

    //region Members

    var checkoutFragment: CheckoutFragment = CheckoutFragment()
    var paymentFragment: PaymentFragment = PaymentFragment()
    var trackFragment: TrackFragment = TrackFragment()
    var active = BaseFragment()


    //endregion

    //region Events
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        init()

    }

    private fun init() {
        supportFragmentManager.beginTransaction().replace(R.id.layout_container, checkoutFragment, "checkoutFragment").addToBackStack("checkoutFragment").commit()

        ivBackCheckout.setOnClickListener({
            onBackPressed()
        })
    }

    //
//    override fun onBackPressed() {
//        super.onBackPressed()
//        val count = supportFragmentManager.backStackEntryCount
//
//        if (count == 0) {
//            super.onBackPressed()
//            finish()
//            //additional code
//        } else {
//            supportFragmentManager.popBackStack()
//        }
//
//
//    }
    override fun onBackPressed() {

        val fragmentList = supportFragmentManager.fragments

        var handled = false
        for (f in fragmentList) {
            if (f is CheckoutFragment) {
                handled = true
              finish()
            }
            else if (f is PaymentFragment) {
                supportFragmentManager.beginTransaction().replace(R.id.layout_container, checkoutFragment, "checkoutFragment").commit()
                handled = true
                if (handled) {
                    break
                }
            }
        }

        if (!handled) {
            super.onBackPressed()
            finish()
        }
    }

    //endregion

    //region Helper Functions4

    //endregion

}
