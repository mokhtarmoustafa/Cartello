package com.twoam.cartello.View

import android.content.Intent
import android.graphics.drawable.RippleDrawable
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.RelativeLayout
import com.twoam.cartello.R
import com.twoam.cartello.Utilities.DB.PreferenceController
import com.twoam.cartello.Utilities.General.AppConstants

class IntroductionActivity : AppCompatActivity() {

    //region Class Members
    private lateinit var rippleLayout: RelativeLayout
    private var startTime: Long = 2000
    //endregion

    //region Events
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_introduction)

        init()
        logOUt()
    }

    private fun logOUt() {
        PreferenceController.getInstance(applicationContext).clear(AppConstants.IS_LOGIN)
    }
    //endregion

    //region   Helper Functions
    private fun init() {
        rippleLayout = findViewById(R.id.linearLayout)

        Handler().postDelayed({
            runOnUiThread {
                startAnimate(rippleLayout)
            }

        }, 500)

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
        // Check if user is already logged in or
        var t = PreferenceController.getInstance(applicationContext)["t"]
        var login = PreferenceController.getInstance(applicationContext)[AppConstants.IS_LOGIN]
        var user = PreferenceController.getInstance(applicationContext).getUserPref(AppConstants.USER_DATA)
        if (PreferenceController.getInstance(applicationContext)[AppConstants.IS_LOGIN] != null &&
                PreferenceController.getInstance(applicationContext)[AppConstants.IS_LOGIN] == AppConstants.TRUE) {
            getUserData()
            finish()
        } else {
            startActivity(Intent(applicationContext, LoginActivity::class.java))
            finish()
        }
    }

    private fun getUserData() {

        var currentUser = PreferenceController.getInstance(applicationContext).getUserPref(AppConstants.USER_DATA)!!

        if (currentUser != null && currentUser.hasdAddress) {
            AppConstants.CurrentLoginUser = currentUser
            startActivity(Intent(applicationContext, MainActivity::class.java))
        } else {
            startActivity(Intent(applicationContext, NewAddressActivity::class.java))
        }
    }
    //endregion

}
