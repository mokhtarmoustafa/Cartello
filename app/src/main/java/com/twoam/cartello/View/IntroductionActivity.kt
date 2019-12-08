package com.twoam.cartello.View

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.RelativeLayout
import com.skyfishjy.library.RippleBackground
import com.twoam.cartello.Model.Cart
import com.twoam.cartello.Model.User
import com.twoam.cartello.R
import com.twoam.cartello.Utilities.DB.PreferenceController
import com.twoam.cartello.Utilities.Exception.CrashHandeller
import com.twoam.cartello.Utilities.General.AppConstants
import java.lang.Exception


class IntroductionActivity : AppCompatActivity() {

    //region Class Members
    private var startTime: Long = 3000
    private lateinit var rippleBackground: RippleBackground
    //endregion

    //region Events
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_introduction)
        CrashHandeller.deploy(this, CrashActivity::class.java)
        init()
    }


    //endregion

    //region   Helper Functions
    private fun init() {
        Cart.emptyCart()
        rippleBackground = findViewById(R.id.content)

        rippleBackground.startRippleAnimation()

        Handler().postDelayed({

            runOnUiThread {
                checkedUserLogged()
            }

        }, startTime)
    }


    private fun checkedUserLogged() {

//        PreferenceController.getInstance(applicationContext).clear(AppConstants.USER_DATA)

        // Check if user is already logged in or not
        var user = PreferenceController.getInstance(applicationContext).getUserPref(AppConstants.USER_DATA)
        if (user != null
                && PreferenceController.getInstance(applicationContext)[AppConstants.IS_LOGIN] != null
                && PreferenceController.getInstance(applicationContext)[AppConstants.IS_LOGIN] == AppConstants.TRUE) {
            getUserData(user)
            finish()
            rippleBackground.stopRippleAnimation()
        } else {
            startActivity(Intent(this@IntroductionActivity, LoginActivity::class.java))
            finish()
            rippleBackground.stopRippleAnimation()
        }
    }

    private fun getUserData(currentUser: User) {
        try {

            AppConstants.CurrentLoginUser = currentUser
            if (currentUser.addresses!!.size > 0) {
                startActivity(Intent(applicationContext, MainActivity::class.java))
                finish()
            } else {
                startActivity(Intent(applicationContext, CreateAddressActivity::class.java))
                finish()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }


    }
    //endregion

}
