package com.twoam.cartello.View

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.View
import android.widget.Button
import com.twoam.cartello.R
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.twoam.Networking.INetworkCallBack
import com.twoam.Networking.NetworkManager
import com.twoam.cartello.Model.User
import com.twoam.cartello.Utilities.API.ApiResponse
import com.twoam.cartello.Utilities.API.ApiServices
import com.twoam.cartello.Utilities.DB.PreferenceController
import com.twoam.cartello.Utilities.General.AppConstants
import com.twoam.cartello.Utilities.General.CustomEditTextDatePicker
import de.hdodenhof.circleimageview.CircleImageView


class SignUpActivity : AppCompatActivity(), View.OnClickListener {

    //region  Members
    private lateinit var tvSignIn: TextView
    private lateinit var tvSignUp: TextView
    private lateinit var etEmail: EditText
    private lateinit var tvEmailError: TextView
    private lateinit var etFullName: EditText
    private lateinit var tvFullNameError: TextView
    private lateinit var etPhoneNumber: EditText
    private lateinit var tvPhoneNumberError: TextView
    private lateinit var etBirthDate: EditText
    private lateinit var tvPBirthDateError: TextView
    private lateinit var etPassword: EditText
    private lateinit var tvPasswordError: TextView
    private lateinit var etConfirmPassword: EditText
    private lateinit var tvConfirmPasswordError: TextView
    private lateinit var tvTermsConditions: TextView
    private lateinit var btnSignUp: Button
    private lateinit var ivFacebook: CircleImageView
    private lateinit var ivGoogle: CircleImageView
    private lateinit var birthDate: CustomEditTextDatePicker

    private var user: User = User()
    //endregion

    //region Events
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        init()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tvSignIn -> {
                startActivity(Intent(this, LoginActivity::class.java))
                overridePendingTransition(R.anim.enter, R.anim.exit)
                finish()
            }

            R.id.btnSignUp -> {
                var email = etEmail.text.toString()
                var fullName = etFullName.text.toString()
                var phoneNumber = etPhoneNumber.text.toString()
                var birthDate = etBirthDate.text.toString()
                var password = etPassword.text.toString()
                var confirmPassword = etConfirmPassword.text.toString()

                var valid = validateUserData(email, fullName, phoneNumber, birthDate, password, confirmPassword)

                if (valid) {
                    signUp(fullName, email, phoneNumber, birthDate, password, confirmPassword)
                }
            }
            R.id.ivFacebook -> {
            }
            R.id.ivGoogle -> {
            }
            R.id.tvTermsAndConditions -> {
            }

        }
    }

    //endregion

    //region Helper Functions
    private fun init() {

        tvSignIn = findViewById(R.id.tvSignIn)
        tvSignUp = findViewById(R.id.tvSignUp)

        etEmail = findViewById(R.id.etEmail)
        etFullName = findViewById(R.id.etFullName)
        etPhoneNumber = findViewById(R.id.etPhoneNumber)
        etBirthDate = findViewById(R.id.etDateOfBirth)
        birthDate = CustomEditTextDatePicker(this, etBirthDate.id)
        etPassword = findViewById(R.id.etPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)

        tvEmailError = findViewById(R.id.tvEmailError)
        tvFullNameError = findViewById(R.id.tvFullNameError)
        tvPhoneNumberError = findViewById(R.id.tvPhoneNumberError)
        tvPBirthDateError = findViewById(R.id.tvDateOfBirthError)
        tvPasswordError = findViewById(R.id.tvPasswordError)
        tvConfirmPasswordError = findViewById(R.id.tvConfirmPasswordError)
        tvTermsConditions = findViewById(R.id.tvTermsAndConditions)
        ivFacebook = findViewById(R.id.ivFacebook)
        ivGoogle = findViewById(R.id.ivGoogle)

        btnSignUp = findViewById(R.id.btnSignUp)

        tvSignIn.setOnClickListener(this)
        tvSignUp.setOnClickListener(this)
        ivFacebook.setOnClickListener(this)
        ivGoogle.setOnClickListener(this)
        tvTermsConditions.setOnClickListener(this)
        btnSignUp.setOnClickListener(this)

    }

    fun isValidEmail(email: CharSequence): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                .matches()
    }


    private fun validateUserData(email: String, fullName: String, phoneNumber: String, birthDate: String, password: String, confirmPassword: String): Boolean {
        var valid = false

        if (email.isNullOrEmpty() || !isValidEmail(email)) {
            tvEmailError.visibility = View.VISIBLE
            tvFullNameError.visibility = View.INVISIBLE
            tvPBirthDateError.visibility = View.INVISIBLE
            tvPhoneNumberError.visibility = View.INVISIBLE
            tvPasswordError.visibility = View.INVISIBLE
            tvConfirmPasswordError.visibility = View.INVISIBLE


            valid = false
        } else if (fullName.isNullOrEmpty()) {
            tvFullNameError.visibility = View.VISIBLE
            tvEmailError.visibility = View.INVISIBLE
            tvPBirthDateError.visibility = View.INVISIBLE
            tvPhoneNumberError.visibility = View.INVISIBLE
            tvPasswordError.visibility = View.INVISIBLE
            tvConfirmPasswordError.visibility = View.INVISIBLE
            valid = false
        } else if (phoneNumber.isNullOrEmpty()) {
            tvPhoneNumberError.visibility = View.VISIBLE
            tvFullNameError.visibility = View.INVISIBLE
            tvEmailError.visibility = View.INVISIBLE
            tvPBirthDateError.visibility = View.INVISIBLE
            tvPasswordError.visibility = View.INVISIBLE
            tvConfirmPasswordError.visibility = View.INVISIBLE
            tvPhoneNumberError.requestFocus()
            valid = false
        } else if (birthDate.isNullOrEmpty()) {
            tvPBirthDateError.visibility = View.VISIBLE
            tvPhoneNumberError.visibility = View.INVISIBLE
            tvFullNameError.visibility = View.INVISIBLE
            tvEmailError.visibility = View.INVISIBLE
            tvPasswordError.visibility = View.INVISIBLE
            valid = false
        } else if (password.isNullOrEmpty()) {
            tvPasswordError.visibility = View.VISIBLE
            tvFullNameError.visibility = View.INVISIBLE
            tvEmailError.visibility = View.INVISIBLE
            tvPBirthDateError.visibility = View.INVISIBLE
            tvPhoneNumberError.visibility = View.INVISIBLE
            tvConfirmPasswordError.visibility = View.INVISIBLE

            valid = false
        } else if (confirmPassword.isNullOrEmpty() || password != confirmPassword) {
            tvConfirmPasswordError.visibility = View.VISIBLE
            tvFullNameError.visibility = View.INVISIBLE
            tvEmailError.visibility = View.INVISIBLE
            tvPBirthDateError.visibility = View.INVISIBLE
            tvPhoneNumberError.visibility = View.INVISIBLE
            tvPasswordError.visibility = View.INVISIBLE

            valid = false
        } else {
            tvConfirmPasswordError.visibility = View.INVISIBLE
            tvFullNameError.visibility = View.INVISIBLE
            tvEmailError.visibility = View.INVISIBLE
            tvPBirthDateError.visibility = View.INVISIBLE
            tvPhoneNumberError.visibility = View.INVISIBLE
            tvPasswordError.visibility = View.INVISIBLE
            valid = true
        }
        return valid
    }


    private fun signUp(fullName: String, email: String, phoneNumber: String, birthDate: String, password: String, confirmPassword: String): User {
        if (NetworkManager().isNetworkAvailable(this)) {
            var request = NetworkManager().create(ApiServices::class.java)
            var endPoint = request.signUp(fullName, email, phoneNumber, birthDate, password, confirmPassword)
            NetworkManager().request(endPoint, object : INetworkCallBack<ApiResponse<User>> {
                override fun onFailed(error: String) {
                    showDialouge(getString(R.string.error_no_internet))
                }

                override fun onSuccess(response: ApiResponse<User>) {
                    var response = response
                    if (response.code == AppConstants.CODE_200 && response.data != null) {
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
        return user
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
