package com.twoam.cartello.View

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.twoam.Networking.INetworkCallBack
import com.twoam.Networking.NetworkManager
import com.twoam.cartello.Model.User
import com.twoam.cartello.R
import com.twoam.cartello.Utilities.API.ApiResponse
import com.twoam.cartello.Utilities.API.ApiServices
import com.twoam.cartello.Utilities.Base.BaseDefaultActivity
import com.twoam.cartello.Utilities.General.AppConstants
import kotlinx.android.synthetic.main.activity_forget_password.*
import android.view.inputmethod.EditorInfo


class ForgetPasswordActivity : BaseDefaultActivity(), View.OnClickListener {

    //region Members
    private var mode = 0 //0 valid email 1 verfiy code 2 reset password
    private var code: String = ""
    private var email: String = ""
    private var password: String = ""
    private var confirmPassword: String = ""
    private var verifyPassword: String = ""


    private var user: User = User()

    //endregion

    //region Events
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)


        init()

        changePasswordMode(intent.getIntExtra(AppConstants.CHANGE_PASSWORD, 0))

    }


    override fun onClick(v: View?) {
        when (v?.id) {

            R.id.ivBackForgetPassword -> {
                mode -= 1

                if (mode == 0) {
                    rlEmail.visibility = View.VISIBLE
                    btnSubmit.visibility = View.VISIBLE
                    etEmail.requestFocus()

                    tvInfo.visibility = View.INVISIBLE
                    rlCode.visibility = View.INVISIBLE
                    btnResend.visibility = View.INVISIBLE


                    rlPassword.visibility = View.INVISIBLE
                    rlConfirmPassword.visibility = View.INVISIBLE
                    btnLogIn.visibility = View.INVISIBLE

                } else if (mode == 1) {

                    rlEmail.visibility = View.INVISIBLE
                    btnSubmit.visibility = View.VISIBLE


                    tvInfo.visibility = View.VISIBLE
                    rlCode.visibility = View.VISIBLE
                    btnResend.visibility = View.VISIBLE
                    etCode.requestFocus()

                    rlPassword.visibility = View.INVISIBLE
                    rlConfirmPassword.visibility = View.INVISIBLE
                    btnLogIn.visibility = View.INVISIBLE
                } else if (mode == 2) {
                    rlEmail.visibility = View.INVISIBLE
                    btnSubmit.visibility = View.INVISIBLE

                    tvInfo.visibility = View.INVISIBLE
                    rlCode.visibility = View.INVISIBLE
                    btnResend.visibility = View.INVISIBLE
                    etCode.requestFocus()

                    rlPassword.visibility = View.VISIBLE
                    rlConfirmPassword.visibility = View.VISIBLE
                    btnLogIn.visibility = View.VISIBLE

                    etPassword.requestFocus()
                } else if (mode == 4) {//change password mode
                    finish()
                }


            }
            R.id.btnResend -> {
                email = etEmail.text.toString()
                var valid = validateUserData(email)
                if (valid) {
                    showDialogue()
                    forgetPassowrd(email)
                }

            }

            R.id.btnSubmit -> {
                if (mode == 0)//valid email
                {
                    email = etEmail.text.toString()
                    var valid = validateUserData(email)
                    if (valid) {
                        showDialogue()
                        forgetPassowrd(email)
                    }
                }
                if (mode == 1) //verify code
                {
                    code = etCode.text.toString()
                    var valid = validateCode(code)
                    if (valid) {
                        showDialogue()
                        validateRequestCode(code, email)
                    }

                }

                if (mode == 4) {
                    password = etPassword.text.toString()
                    confirmPassword = etConfirmPassword.text.toString()
                    verifyPassword = etVerifyPassword.text.toString()

                    var valid = validatePassword(password, confirmPassword, verifyPassword)

                    if (valid) {
                        changePassword(password, confirmPassword)
                    }
                }
            }

            R.id.btnLogIn -> {
                password = etPassword.text.toString()
                confirmPassword = etConfirmPassword.text.toString()

                var valid = validatePassword(password, confirmPassword, "not applicable")
                if (valid) {
                    showDialogue()
                    resetPassword(email, code, password)
                }

            }
        }
    }
    //endregion

    //region Helper Functions

    private fun init() {
        ivBackForgetPassword.setOnClickListener(this)
        btnResend.setOnClickListener(this)
        btnSubmit.setOnClickListener(this)
        btnLogIn.setOnClickListener(this)
        etEmail.requestFocus()


    }


    private fun validateUserData(email: String): Boolean {
        var valid = false

        if (email.isNullOrEmpty() || !isValidEmail(email)) {
            tvEmailError.visibility = View.VISIBLE
            tvPasswordError.visibility = View.INVISIBLE
            valid = false
        } else {
            tvEmailError.visibility = View.INVISIBLE
            tvPasswordError.visibility = View.INVISIBLE
            valid = true
        }
        return valid
    }

    private fun validateCode(code: String): Boolean {
        var valid = false

        if (code.isNullOrEmpty()) {
            tvCodeError.visibility = View.VISIBLE
            valid = false
        } else {
            tvCodeError.visibility = View.INVISIBLE
            valid = true
        }

        return valid
    }

    private fun validatePassword(password: String, confirmPassword: String, verifyPassword: String): Boolean {
        var valid = false

        if (password.isNullOrEmpty()) {
            tvPasswordError.visibility = View.VISIBLE
            etPassword.requestFocus()
            tvConfirmPasswordError.visibility = View.INVISIBLE
            tvVerifyPasswordError.visibility = View.INVISIBLE
            valid = false
        } else if (confirmPassword.isNullOrEmpty()) {
            tvConfirmPasswordError.visibility = View.VISIBLE
            etConfirmPassword.requestFocus()
            tvPasswordError.visibility = View.INVISIBLE
            tvVerifyPasswordError.visibility = View.INVISIBLE
            valid = false
        } else if (mode == 4 && verifyPassword.isNullOrEmpty()) {
            tvPasswordError.visibility = View.INVISIBLE
            tvConfirmPasswordError.visibility = View.INVISIBLE
            tvVerifyPasswordError.visibility = View.VISIBLE
            etVerifyPassword.requestFocus()


            valid = false
        } else {

            if (!password.isNullOrEmpty() && !confirmPassword.isNullOrEmpty()) {
                if (password == confirmPassword) {
                    tvConfirmPasswordError.visibility = View.INVISIBLE
                    tvPasswordError.visibility = View.INVISIBLE
                    valid = true
                } else {
                    tvPasswordError.visibility = View.INVISIBLE
                    tvConfirmPasswordError.visibility = View.VISIBLE
                    etConfirmPassword.requestFocus()

                    valid = false
                }
            }
        }

        return valid
    }

    fun isValidEmail(email: CharSequence): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                .matches()
    }

    private fun forgetPassowrd(email: String): Boolean {
        var validEmail = false
        if (NetworkManager().isNetworkAvailable(this)) {
            var request = NetworkManager().create(ApiServices::class.java)
            var endPoint = request.forgetPassword(email)
            NetworkManager().request(endPoint, object : INetworkCallBack<ApiResponse<Boolean>> {
                override fun onFailed(error: String) {
                    hideDialogue()
                    showAlertDialouge(getString(R.string.error_no_internet))
                }

                override fun onSuccess(response: ApiResponse<Boolean>) {
                    if (response.code == AppConstants.CODE_200) {
                        validEmail = true
                        mode = 1
                        hideDialogue()
                        Toast.makeText(applicationContext, response.message, Toast.LENGTH_SHORT).show()
                        validEmailStep(email)
                        verfiyCodeStep()
                    } else {
                        hideDialogue()
                        Toast.makeText(applicationContext, getString(R.string.error_email_not_registered), Toast.LENGTH_SHORT).show()
                    }

                }
            })

        } else {
            hideDialogue()
            showAlertDialouge(getString(R.string.error_no_internet))
        }
        return validEmail
    }

    private fun validateRequestCode(code: String, email: String): Boolean {
        var validEmail = false
        if (NetworkManager().isNetworkAvailable(this)) {
            var request = NetworkManager().create(ApiServices::class.java)
            var endPoint = request.validateRequestCode(code, email)
            NetworkManager().request(endPoint, object : INetworkCallBack<ApiResponse<Boolean>> {
                override fun onFailed(error: String) {
                    hideDialogue()
                    showAlertDialouge(getString(R.string.error_no_internet))
                }

                override fun onSuccess(response: ApiResponse<Boolean>) {
                    if (response.code == AppConstants.CODE_200) {
                        mode = 2
                        validEmail = true
                        hideDialogue()
                        resetPasswordStep()

                    } else {
                        hideDialogue()
                        Toast.makeText(applicationContext, getString(R.string.error_incorrect_code), Toast.LENGTH_SHORT).show()
                    }

                }
            })

        } else {
            hideDialogue()
            showAlertDialouge(getString(R.string.error_no_internet))
        }
        return validEmail
    }

    private fun resetPassword(email: String, code: String, password: String): User {

        if (NetworkManager().isNetworkAvailable(this)) {
            var request = NetworkManager().create(ApiServices::class.java)
            var endPoint = request.resetPassword(email, code, password)
            NetworkManager().request(endPoint, object : INetworkCallBack<ApiResponse<User>> {
                override fun onFailed(error: String) {
                    hideDialogue()
                    showAlertDialouge(getString(R.string.error_no_internet))
                }

                override fun onSuccess(response: ApiResponse<User>) {
                    if (response.code == AppConstants.CODE_200) {
                        mode = 3
                        user = response.data!!
                        hideDialogue()
                        saveUserData(user)

                    } else {
                        hideDialogue()
                        Toast.makeText(applicationContext, getString(R.string.error_incorrect_code), Toast.LENGTH_SHORT).show()
                    }

                }
            })

        } else {
            hideDialogue()
            showAlertDialouge(getString(R.string.error_no_internet))
        }
        return user
    }

    private fun validEmailStep(email: String) {
        rlEmail.visibility = View.GONE
        tvInfo.visibility = View.VISIBLE
        tvCodeError.visibility = View.INVISIBLE
        btnResend.visibility = View.VISIBLE
        rlCode.visibility = View.VISIBLE
        etCode.setText("")
        tvInfo.text = ""

        tvInfo.text = getString(R.string.info) + " " + email

    }

    private fun verfiyCodeStep() {
        rlCode.visibility = View.VISIBLE
        etCode.requestFocus()
        tvInfo.visibility = View.VISIBLE
    }

    private fun resetPasswordStep() {

        rlCode.visibility = View.INVISIBLE
        tvInfo.visibility = View.INVISIBLE
        btnResend.visibility = View.INVISIBLE
        btnSubmit.visibility = View.INVISIBLE
        btnLogIn.visibility = View.VISIBLE
        rlPassword.visibility = View.VISIBLE
        rlConfirmPassword.visibility = View.VISIBLE
        etPassword.requestFocus()

    }

    private fun changePasswordMode(mode: Int) {

        if (mode == 4) {
            this.mode = mode
            user = AppConstants.CurrentLoginUser

            rlEmail.visibility = View.INVISIBLE
            rlPassword.visibility = View.VISIBLE
            rlConfirmPassword.visibility = View.VISIBLE
            rlVerifyPassword.visibility = View.VISIBLE


            tvTitle.text = getString(R.string.change_password)
            btnSubmit.text = getString(R.string.save)
            tvHintPassword.text = getString(R.string.current_password)
            tvPasswordError.text = getString(R.string.required)
            etConfirmPassword.imeOptions = EditorInfo.IME_ACTION_NEXT or EditorInfo.IME_FLAG_NO_EXTRACT_UI
            etVerifyPassword.imeOptions = EditorInfo.IME_ACTION_DONE or EditorInfo.IME_FLAG_NO_EXTRACT_UI

        }
    }

    private fun changePassword(oldPassword: String, newPasswotd: String) {
        if (NetworkManager().isNetworkAvailable(this)) {
            var authorization = AppConstants.BEARER + user.token
            var request = NetworkManager().create(ApiServices::class.java)
            var endPoint = request.changePassword(authorization, oldPassword, newPasswotd)
            NetworkManager().request(endPoint, object : INetworkCallBack<ApiResponse<Boolean>> {
                override fun onFailed(error: String) {
                    hideDialogue()
                    showAlertDialouge(getString(R.string.error_no_internet))
                }

                override fun onSuccess(response: ApiResponse<Boolean>) {
                    if (response.code == AppConstants.CODE_200) {
                        hideDialogue()
                        saveUserData(user)
                        finish()

                    } else {
                        hideDialogue()
                        Toast.makeText(applicationContext, getString(R.string.error_incorrect_code), Toast.LENGTH_SHORT).show()
                    }

                }
            })

        } else {
            hideDialogue()
            showAlertDialouge(getString(R.string.error_no_internet))
        }

    }
    //endregion

}
