package com.twoam.cartello.View

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import com.twoam.cartello.R

class LoginActivity : AppCompatActivity() {

    //region Members
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var tvEmailError: TextView
    private lateinit var tvPasswordError: TextView
    private lateinit var tvForgetPassword:TextView
    private lateinit var tvSkipNow:TextView



    //endregion

    //region Events
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

    }

    //endregion

    //region Helper Functions
    //endregion


    //region  helper functions
    private fun validateUserData(email: String, password: String): Boolean {
        var valid = false

        if (email.isNullOrEmpty()) {

        }
        return valid
    }

    //endregion

}
