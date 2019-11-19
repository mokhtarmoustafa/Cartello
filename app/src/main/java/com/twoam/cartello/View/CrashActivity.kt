package com.twoam.cartello.View

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.twoam.cartello.R

class CrashActivity : AppCompatActivity() {

    private var btnRestart: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crash)

        btnRestart=findViewById(R.id.btnRestart)

        btnRestart?.setOnClickListener {
            startActivity(Intent(this@CrashActivity, IntroductionActivity::class.java))
            finish()
        }
    }
}
