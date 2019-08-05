package com.twoam.cartello.View

import android.content.Intent
import android.graphics.drawable.RippleDrawable
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.RelativeLayout
import com.skyfishjy.library.RippleBackground
import com.twoam.cartello.Model.City
import com.twoam.cartello.Model.User
import com.twoam.cartello.R
import com.twoam.cartello.Utilities.DB.PreferenceController
import com.twoam.cartello.Utilities.General.AppConstants
import com.twoam.cartello.Utilities.General.AppController


class IntroductionActivity : AppCompatActivity() {

    //region Class Members
    private lateinit var rippleLayout: RelativeLayout
    private var startTime: Long = 3000
    private lateinit var  rippleBackground:RippleBackground
    //endregion

    //region Events
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_introduction)

        init()
    }


    //endregion

    //region   Helper Functions
    private fun init() {
//        rippleLayout = findViewById(R.id.linearLayout)
        rippleBackground=findViewById(R.id.content)

//        Handler().postDelayed({
//            runOnUiThread {
//                startAnimate(rippleLayout)
//            }
//
//        }, 500)
        rippleBackground.startRippleAnimation()

        Handler().postDelayed({

            runOnUiThread {
                checkedUserLogged()
            }

        }, startTime)
    }

    private fun startAnimate(view: View) {
        val rippleDrawable = view.background

        if (Build.VERSION.SDK_INT >= 21 && rippleDrawable is RippleDrawable) {

            rippleDrawable.state = intArrayOf(android.R.attr.state_pressed, android.R.attr.state_enabled)

            val handler = Handler()

            handler.postDelayed({ rippleDrawable.state = intArrayOf() }, 1100)
        }
    }

    private fun checkedUserLogged() {

        var test = PreferenceController.getInstance(applicationContext)[AppConstants.TEST_MODE]

        if (test.isNullOrEmpty()) {
            PreferenceController.getInstance(AppController.getContext()).clear(AppConstants.IS_LOGIN)
            PreferenceController.getInstance(AppController.getContext()).clear(AppConstants.USER_DATA)
            PreferenceController.getInstance(AppController.getContext()).Set(AppConstants.TEST_MODE, AppConstants.TRUE)
        }

        // Check if user is already logged in or not
        var user = PreferenceController.getInstance(applicationContext).getUserPref(AppConstants.USER_DATA)
        if (user != null
                && PreferenceController.getInstance(applicationContext)[AppConstants.IS_LOGIN] != null
                && PreferenceController.getInstance(applicationContext)[AppConstants.IS_LOGIN] == AppConstants.TRUE) {
            getUserData(user)
            finish()
            rippleBackground.stopRippleAnimation()
        } else {
            startActivity(Intent(applicationContext, LoginActivity::class.java))
            finish()
            rippleBackground.stopRippleAnimation()
        }
    }

    private fun getUserData(currentUser: User) {


       var cities = PreferenceController.getInstance(AppController.getContext()).getCitiesPref(AppConstants.CITIES_DATA)!!


        if (currentUser.address!!.addresses!!.count() > 0 && cities.count() > 0) {
            AppConstants.CurrentLoginUser = currentUser
            startActivity(Intent(applicationContext, MainActivity::class.java))
            finish()
        } else {
            startActivity(Intent(applicationContext, CreateAddressActivity::class.java))
            finish()
        }
    }
    //endregion

}
