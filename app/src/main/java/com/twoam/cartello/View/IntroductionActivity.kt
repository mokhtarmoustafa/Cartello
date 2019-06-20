package com.twoam.cartello.View

import android.content.Intent
import android.graphics.drawable.RippleDrawable
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.RelativeLayout
import android.widget.Toast
import com.twoam.cartello.MainActivity

import com.twoam.cartello.R
import com.twoam.cartello.Utilities.DB.PreferenceController
import com.twoam.cartello.Utilities.General.AppConstants

class IntroductionActivity : AppCompatActivity() {


    private lateinit var rippleLayout: RelativeLayout
    private var startTime: Long = 2000


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_introduction)

        init()

    }

    private fun init() {
        rippleLayout = findViewById(R.id.linearLayout)

        Handler().postDelayed({
            runOnUiThread {
                startAnimate(rippleLayout)
            }

        }, 500)

        Handler().postDelayed({

            runOnUiThread {
                //                Toast.makeText(applicationContext, "Go to main", Toast.LENGTH_SHORT).show()
                navigate()
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

    private fun navigate()
    {
        // Check if user is already logged in or not
        if (PreferenceController.getInstance(applicationContext)[PreferenceController.LOGIN] != null &&
                PreferenceController.getInstance(applicationContext)[PreferenceController.LOGIN] == AppConstants.TRUE) {
            startActivity(Intent(applicationContext, MainActivity::class.java))
            finish()
        }
        else
        {
            startActivity(Intent(applicationContext, LoginActivity::class.java))
            finish()
        }


        finish()
    }
}
