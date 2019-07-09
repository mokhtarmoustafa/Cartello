package com.twoam.cartello.View

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.LayerDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.twoam.cartello.R
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        //CHANGE THE RATING BAR COLOR
        val stars = rbRate.progressDrawable as LayerDrawable
        stars.getDrawable(2).setColorFilter(Color.parseColor("#ff9600"), PorterDuff.Mode.SRC_ATOP)
    }
}
