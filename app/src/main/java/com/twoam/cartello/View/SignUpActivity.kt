package com.twoam.cartello.View

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.twoam.cartello.R
import com.facebook.*
import com.twoam.Networking.INetworkCallBack
import com.twoam.Networking.NetworkManager
import com.twoam.cartello.Model.User
import com.twoam.cartello.Utilities.API.ApiResponse
import com.twoam.cartello.Utilities.API.ApiServices
import com.twoam.cartello.Utilities.General.AppConstants
import com.twoam.cartello.Utilities.General.CustomEditTextDatePicker
import de.hdodenhof.circleimageview.CircleImageView
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.GooglePlayServicesUtil
import com.google.android.gms.common.api.GoogleApiClient
import com.twoam.cartello.Utilities.General.AnimateScroll
import com.twoam.cartello.Utilities.Base.BaseDefaultActivity
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class SignUpActivity : BaseDefaultActivity(), View.OnClickListener {

    //region  Members
    private lateinit var scrollView:ScrollView
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
    private val TAG = SignUpActivity::class.java.simpleName
    private var callbackManager: CallbackManager? = null
    private var accessToken: AccessToken? = null
    private var profileTracker: ProfileTracker? = null
    private var imageUrl: Uri? = null
    private val RC_SIGN_IN = 0
    private var account: GoogleSignInAccount? = null
    private var mGoogleApiClient: GoogleApiClient? = null
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
                    showDialogue()
                    signUp(fullName, email, phoneNumber, birthDate, password, confirmPassword)
                }
            }
            R.id.ivFacebook -> {
                showDialogue()
                if (NetworkManager().isNetworkAvailable(this)) {
                    socialLoginFacebook()
                } else {
                    hideDialogue()
                    showAlertDialouge(getString(R.string.error_no_internet))

                }
            }
            R.id.ivGoogle -> {
                showDialogue()
                if (NetworkManager().isNetworkAvailable(this)) {
                    socialLoginGoogle()
                } else {
                    hideDialogue()
                    showAlertDialouge(getString(R.string.error_no_internet))

                }
            }
            R.id.tvTermsAndConditions -> {
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            handleGoogleSignInResult(result)
        } else if (FacebookSdk.isFacebookRequestCode(requestCode)) {
            callbackManager!!.onActivityResult(requestCode, resultCode, data)
        }

    }
    //endregion

    //region Helper Functions
    private fun init() {

        scrollView=findViewById(R.id.scrollView)
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
            AnimateScroll.scrollToView(scrollView,etEmail)

            valid = false
        } else if (fullName.isNullOrEmpty()) {
            tvFullNameError.visibility = View.VISIBLE
            tvEmailError.visibility = View.INVISIBLE
            tvPBirthDateError.visibility = View.INVISIBLE
            tvPhoneNumberError.visibility = View.INVISIBLE
            tvPasswordError.visibility = View.INVISIBLE
            tvConfirmPasswordError.visibility = View.INVISIBLE
            AnimateScroll.scrollToView(scrollView,etFullName)
            valid = false
        } else if (phoneNumber.isNullOrEmpty()) {
            tvPhoneNumberError.visibility = View.VISIBLE
            tvFullNameError.visibility = View.INVISIBLE
            tvEmailError.visibility = View.INVISIBLE
            tvPBirthDateError.visibility = View.INVISIBLE
            tvPasswordError.visibility = View.INVISIBLE
            tvConfirmPasswordError.visibility = View.INVISIBLE
            tvPhoneNumberError.requestFocus()
            AnimateScroll.scrollToView(scrollView,etPhoneNumber)
            valid = false
        } else if (birthDate.isNullOrEmpty()) {
            tvPBirthDateError.visibility = View.VISIBLE
            tvPhoneNumberError.visibility = View.INVISIBLE
            tvFullNameError.visibility = View.INVISIBLE
            tvEmailError.visibility = View.INVISIBLE
            tvPasswordError.visibility = View.INVISIBLE
            AnimateScroll.scrollToView(scrollView,etBirthDate)
            valid = false
        } else if (password.isNullOrEmpty()) {
            tvPasswordError.visibility = View.VISIBLE
            tvFullNameError.visibility = View.INVISIBLE
            tvEmailError.visibility = View.INVISIBLE
            tvPBirthDateError.visibility = View.INVISIBLE
            tvPhoneNumberError.visibility = View.INVISIBLE
            tvConfirmPasswordError.visibility = View.INVISIBLE
            AnimateScroll.scrollToView(scrollView,etPassword)
            valid = false
        } else if (confirmPassword.isNullOrEmpty() || password != confirmPassword) {
            tvConfirmPasswordError.visibility = View.VISIBLE
            tvFullNameError.visibility = View.INVISIBLE
            tvEmailError.visibility = View.INVISIBLE
            tvPBirthDateError.visibility = View.INVISIBLE
            tvPhoneNumberError.visibility = View.INVISIBLE
            tvPasswordError.visibility = View.INVISIBLE
            AnimateScroll.scrollToView(scrollView,etConfirmPassword)
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
                    hideDialogue()
                    showAlertDialouge(getString(R.string.error_no_internet))
                }

                override fun onSuccess(response: ApiResponse<User>) {
                    var response = response
                    if (response.code == AppConstants.CODE_200) {
                        user = response.data!!
                        saveUserData(user)
                    } else {
                        hideDialogue()
                        showAlertDialouge(response.message)
//                        Toast.makeText(applicationContext, response.message, Toast.LENGTH_SHORT).show()
                    }

                }
            })


        } else {
            hideDialogue()
            showAlertDialouge(getString(R.string.error_no_internet))
        }
        return user
    }


    private fun socialLoginFacebook() {
        callbackManager = CallbackManager.Factory.create()
        FacebookSdk.sdkInitialize(this.applicationContext)
        if (callbackManager == null)
            callbackManager = CallbackManager.Factory.create()

        // If the access token is available already assign it.
        accessToken = AccessToken.getCurrentAccessToken()

        profileTracker = object : ProfileTracker() {
            override fun onCurrentProfileChanged(oldProfile: Profile?, currentProfile: Profile?) {
                Log.i(TAG, "Facebook onCurrentAccessTokenChanged")
                if (currentProfile != null) {
                    imageUrl = currentProfile!!.getProfilePictureUri(400, 400)
                }

            }
        }

        // Callback registration
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"))
        LoginManager.getInstance().registerCallback(callbackManager!!, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                // App code
                accessToken = loginResult.accessToken
                val request = GraphRequest.newMeRequest(loginResult.accessToken, GraphRequest.GraphJSONObjectCallback { `object`, response ->
                    Log.i(TAG, response.toString())

                    // Application code
                    try {
                        val jsonObject: JSONObject = `object`
                        val user = User()
                        user.socialType = (User.socialType_Facebook)
                        if (!jsonObject.isNull("email"))
                            user.email = jsonObject.getString("email")
                        if (!jsonObject.isNull("name"))
                            user.name = jsonObject.getString("name")
                        if (!jsonObject.isNull("id"))
                            user.id = jsonObject.getString("id")
                        if (imageUrl != null)
                            user.fullImagePath = imageUrl.toString()

                        saveUserData(user)
                    } catch (e: JSONException) {
                        Log.e(TAG, e.message)
                    }

                })

                val parameters = Bundle()
                parameters.putString("fields", "id,name,email,gender,birthday")
                request.parameters = parameters
                request.executeAsync()
            }

            override fun onCancel() {
                // App code
                Log.e(TAG, "Facebook onCancel")
            }

            override fun onError(exception: FacebookException) {
                Log.e(TAG, exception.toString())
            }
        })

    }

    private fun socialLoginGoogle() {
        if (mGoogleApiClient == null) {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()

            val connectionFailedListener = GoogleApiClient.OnConnectionFailedListener { result ->
                Log.i("onConnectionFailed", "onConnectionFailed")
                if (!result.hasResolution()) {
                    GooglePlayServicesUtil.getErrorDialog(result.errorCode, this, 0).show()
                }
            }
            mGoogleApiClient = GoogleApiClient.Builder(this)
                    .enableAutoManage(this, connectionFailedListener).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build()
            mGoogleApiClient!!.connect()
        }
        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
        startActivityForResult(signInIntent, RC_SIGN_IN)

    }

    private fun handleGoogleSignInResult(result: GoogleSignInResult) {
        Log.i(TAG, result.status.toString())
        if (result.isSuccess) {
            // Signed in successfully, show authenticated UI.
            val acct = result.signInAccount

            val personName = acct!!.displayName

            Log.i(TAG, "Google info " + personName + " " + acct.email + " " + acct.id + " " + acct.photoUrl)

            val user = User()
            user.socialType = User.Companion.socialType_Google
            user.email = acct.email!!
            user.name = personName!!
            if (acct.photoUrl != null)
                user.fullImagePath = acct.photoUrl!!.toString()
            user.id = acct.id!!
            saveUserData(user)

        } else if (!result.isSuccess) {
            Log.e(TAG, "google failed")
        }
    }


    //endregion

}
