package com.twoam.cartello.View

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.twoam.Networking.INetworkCallBack
import com.twoam.Networking.NetworkManager
import com.twoam.cartello.Model.User
import com.twoam.cartello.R
import com.twoam.cartello.Utilities.API.ApiResponse
import com.twoam.cartello.Utilities.API.ApiServices
import com.twoam.cartello.Utilities.DB.PreferenceController
import com.twoam.cartello.Utilities.General.AppConstants

class LoginActivity : AppCompatActivity(), View.OnClickListener {


    //region Members
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var tvEmailError: TextView
    private lateinit var tvPasswordError: TextView
    private lateinit var tvForgetPassword: TextView
    private lateinit var tvSkipNow: TextView
    private lateinit var btnSignIn: Button
    private var user: User = User()


    //endregion

    //region Events
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        init()

    }

    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.tvForgetPassword -> {
            }
            R.id.tvSkipNow -> {
                logInGuest()
            }
            R.id.btnSignUp -> {
                var email = etEmail.text.toString()
                var password = etPassword.text.toString()
                var valid = validateUserData(email, password)
                if (valid) {
                    user = logIn(email, password)
                    AppConstants.CurrentLoginUser = user
                }
            }
        }
    }
    //endregion

    //region Helper Functions
    //endregion


    //region  helper functions

    private fun init() {
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        tvEmailError = findViewById(R.id.tvEmailError)
        tvPasswordError = findViewById(R.id.tvPasswordError)
        tvForgetPassword = findViewById(R.id.tvForgetPassword)
        tvSkipNow = findViewById(R.id.tvSkipNow)
        btnSignIn = findViewById(R.id.btnSignUp)

        tvForgetPassword.setOnClickListener(this)
        tvSkipNow.setOnClickListener(this)
        btnSignIn.setOnClickListener(this)

    }

    private fun validateUserData(email: String, password: String): Boolean {
        var valid = false

        if (email.isNullOrEmpty() || !isValidEmail(email)) {
            tvEmailError.visibility = View.VISIBLE
            tvPasswordError.visibility = View.INVISIBLE
            valid = false
        } else if (password.isNullOrEmpty()) {
            tvEmailError.visibility = View.INVISIBLE
            tvPasswordError.visibility = View.VISIBLE
            valid = false
        } else {
            tvEmailError.visibility = View.INVISIBLE
            tvPasswordError.visibility = View.INVISIBLE
            valid = true
        }
        return valid
    }


    fun isValidEmail(email: CharSequence): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                .matches()
    }

    private fun logIn(email: String, password: String): User {


        if (NetworkManager().isNetworkAvailable(this)) {
            var request = NetworkManager().create(ApiServices::class.java)
            var endPoint = request.logIn(email, password)
            NetworkManager().request(endPoint, object : INetworkCallBack<ApiResponse<User>> {
                override fun onFailed(error: String) {
                    showDialouge(getString(R.string.error_no_internet))
                }

                override fun onSuccess(response: ApiResponse<User>) {
                    if (response.hashCode() == AppConstants.CODE_200 && response.data != null) {
                        user = response.data!!
                        PreferenceController.getInstance(applicationContext).Set(AppConstants.LOGIN, AppConstants.TRUE)
                        PreferenceController.getInstance(applicationContext).setUserPref(AppConstants.USER_DATA, user)

                        checkHasAddress(user!!)
                    } else {
                        Toast.makeText(applicationContext, response.message, Toast.LENGTH_SHORT).show()
                    }

                }
            })

        } else {
            showDialouge(getString(R.string.error_no_internet))
        }
        return user!!
    }

    private fun logInGuest(): User {

        if (NetworkManager().isNetworkAvailable(this)) {
            var request = NetworkManager().create(ApiServices::class.java)
            var endPoint = request.guestLogin()
            NetworkManager().request(endPoint, object : INetworkCallBack<ApiResponse<User>> {
                override fun onFailed(error: String) {
                    showDialouge(getString(R.string.error_no_internet))
                }

                override fun onSuccess(response: ApiResponse<User>) {
                    if (response.message == getString(R.string.success) && response.data != null) {
                        user = response.data!!
                        PreferenceController.getInstance(applicationContext).Set(AppConstants.LOGIN, AppConstants.TRUE)
                        PreferenceController.getInstance(applicationContext).setUserPref(AppConstants.USER_DATA, user)
                        startActivity(Intent(applicationContext, MainActivity::class.java))

                    } else {
                        Toast.makeText(applicationContext, getString(R.string.error_network), Toast.LENGTH_SHORT).show()
                    }

                }
            })

        } else {
            showDialouge(getString(R.string.error_no_internet))
        }
        return user!!
    }

    private fun showDialouge(message: String) {
        var alertDialouge = AlertDialog.Builder(this)
                .setMessage(message)
                .setCancelable(true)
                .setPositiveButton("OK", { dialog, which -> })
        alertDialouge.create()
        alertDialouge.show()
    }

    private fun checkHasAddress(user: User) {

        if (user.addresses.size > 0) {
            startActivity(Intent(this, MainActivity::class.java))
            PreferenceController.getInstance(applicationContext).Set(AppConstants.HASADDRESS, AppConstants.TRUE)
        } else {
            startActivity(Intent(this, CreateAddressActivity::class.java))
        }

    }
    //endregion

}
