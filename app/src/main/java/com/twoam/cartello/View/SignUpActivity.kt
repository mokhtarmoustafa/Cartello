package com.twoam.cartello.View

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.twoam.cartello.R
import android.widget.EditText
import com.twoam.cartello.Utilities.General.MyEditTextDatePicker


class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val editTextFromDate = findViewById<EditText>(R.id.etDateOfBirth)
        val fromDate = MyEditTextDatePicker(this, editTextFromDate.id)
    }
}
