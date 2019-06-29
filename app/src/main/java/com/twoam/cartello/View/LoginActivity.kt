package com.twoam.cartello.View

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.*
import com.facebook.*
import com.twoam.Networking.INetworkCallBack
import com.twoam.Networking.NetworkManager
import com.twoam.cartello.Model.User
import com.twoam.cartello.R
import com.twoam.cartello.Utilities.API.ApiResponse
import com.twoam.cartello.Utilities.API.ApiServices
import com.twoam.cartello.Utilities.DB.PreferenceController
import com.twoam.cartello.Utilities.General.AppConstants
import de.hdodenhof.circleimageview.CircleImageView
import org.json.JSONException
import com.facebook.login.LoginResult
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.GooglePlayServicesUtil
import com.google.android.gms.common.api.GoogleApiClient
import com.twoam.cartello.R.string.email
import com.twoam.cartello.R.string.password
import com.twoam.cartello.Utilities.Base.BaseDefaultActivity
import org.json.JSONObject
import retrofit2.Call
import java.util.*


class LoginActivity : BaseDefaultActivity(), View.OnClickListener {


    //region Members
    private lateinit var tvSignIn: TextView
    private lateinit var tvSignUp: TextView
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var tvEmailError: TextView
    private lateinit var tvPasswordError: TextView
    private lateinit var tvForgetPassword: TextView
    private lateinit var tvSkipNow: TextView
    private lateinit var btnSignIn: Button
    private var ivFacebook: CircleImageView? = null
    private var ivGoogle: CircleImageView? = null
    private var progressBar: ProgressBar? = null
    private var user: User = User()
    private val TAG = LoginActivity::class.java.simpleName
    private var callbackManager: CallbackManager? = null
    private var accessToken: AccessToken? = null
    private var profileTracker: ProfileTracker? = null
    private var imageUrl: Uri? = null
    private val RC_SIGN_IN = 0
    private var account: GoogleSignInAccount? = null
    private var mGoogleApiClient: GoogleApiClient? = null
//    lateinit var mLoadingDialog: Dialog
    //endregion

    //region Events
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        init()
    }

    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.tvSignUp -> {
                startActivity(Intent(this, SignUpActivity::class.java))
                overridePendingTransition(R.anim.enter, R.anim.exit)
                finish()
            }
            R.id.tvForgetPassword -> {
                startActivity(Intent(this, ForgetPasswordActivity::class.java))
            }
            R.id.tvSkipNow -> {
                showDialogue()
                logInGuest()
            }
            R.id.btnSignIn -> {
                var email = etEmail.text.toString()
                var password = etPassword.text.toString()
                var valid = validateUserData(email, password)
                if (valid) {
                    showDialogue()
                    logIn(email, password)

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
        }
    }

    override fun onStart() {
        super.onStart()
//        account = GoogleSignIn.getLastSignedInAccount(this)
//        if (account != null) {
//            // user successfully logged in
//            // Create login session
//            //session!!.setLogin(true)
//            user = User(account!!)
//
//            val intent = Intent(this@LoginActivity, MainActivity::class.java)
//            startActivity(intent)
//            finish()
//        }
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

        tvSignIn = findViewById(R.id.tvSignIn)
        tvSignUp = findViewById(R.id.tvSignUp)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        tvEmailError = findViewById(R.id.tvEmailError)
        tvPasswordError = findViewById(R.id.tvPasswordError)
        tvForgetPassword = findViewById(R.id.tvForgetPassword)
        tvSkipNow = findViewById(R.id.tvSkipNow)
        btnSignIn = findViewById(R.id.btnSignIn)
        ivFacebook = findViewById(R.id.ivFacebook)
        ivGoogle = findViewById(R.id.ivGoogle)
        progressBar = findViewById(R.id.progress_bar)

        tvSignIn.setOnClickListener(this)
        tvSignUp.setOnClickListener(this)
        tvForgetPassword.setOnClickListener(this)
        tvSkipNow.setOnClickListener(this)
        btnSignIn.setOnClickListener(this)
        ivFacebook?.setOnClickListener(this)
        ivGoogle?.setOnClickListener(this)


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
                    hideDialogue()
                    showAlertDialouge(getString(R.string.error_no_internet))
                }

                override fun onSuccess(response: ApiResponse<User>) {
                    if (response.code == AppConstants.CODE_200) {
                        user = response.data!!
                        saveUserData(user)

                    } else {
                        hideDialogue()
                        Toast.makeText(applicationContext, getString(R.string.error_email_password_incorrect), Toast.LENGTH_SHORT).show()
                    }

                }
            })

        } else {
            hideDialogue()
            showAlertDialouge(getString(R.string.error_no_internet))
        }
        return user!!
    }

    private fun logInSocial(access_token: String, socialType: Int): User {
        if (NetworkManager().isNetworkAvailable(this)) {

            var endPoint: Call<ApiResponse<User>>? = null

            var request = NetworkManager().create(ApiServices::class.java)
            endPoint = if (socialType == 1) {
                request.logInSocialFacebook(access_token)
            } else {
                request.logInSocialGoogle(access_token)
            }

            NetworkManager().request(endPoint, object : INetworkCallBack<ApiResponse<User>> {
                override fun onFailed(error: String) {
                    hideDialogue()
                    showAlertDialouge(getString(R.string.error_no_internet))
                }

                override fun onSuccess(response: ApiResponse<User>) {
                    if (response.code == AppConstants.CODE_200) {
                        user = response.data!!
                        saveUserData(user)

                    } else {
                        hideDialogue()
                        Toast.makeText(applicationContext, getString(R.string.error_email_password_incorrect), Toast.LENGTH_SHORT).show()
                    }

                }
            })

        } else {
            hideDialogue()
            showAlertDialouge(getString(R.string.error_no_internet))
        }
        return user!!
    }

    private fun logInGuest(): User {

        if (NetworkManager().isNetworkAvailable(this)) {
            var request = NetworkManager().create(ApiServices::class.java)
            var endPoint = request.guestLogin()
            NetworkManager().request(endPoint, object : INetworkCallBack<ApiResponse<User>> {
                override fun onFailed(error: String) {
                    hideDialogue()
                    showAlertDialouge(getString(R.string.error_no_internet))
                }

                override fun onSuccess(response: ApiResponse<User>) {
                    if (response.message == getString(R.string.success)) {
                        hideDialogue()
                        user = response.data!!
                        PreferenceController.getInstance(applicationContext).Set(AppConstants.IS_LOGIN, AppConstants.TRUE)
                        PreferenceController.getInstance(applicationContext).setUserPref(AppConstants.USER_DATA, user)
                        startActivity(Intent(applicationContext, MainActivity::class.java))
                        finish()
                    } else {
                        hideDialogue()
                        Toast.makeText(applicationContext, getString(R.string.error_network), Toast.LENGTH_SHORT).show()
                    }

                }
            })

        } else {
            hideDialogue()
            showAlertDialouge(getString(R.string.error_no_internet))
        }
        return user!!
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
                accessToken = AccessToken.getCurrentAccessToken()
//                accessToken = loginResult.accessToken
                val request = GraphRequest.newMeRequest(loginResult.accessToken, GraphRequest.GraphJSONObjectCallback { `object`, response ->
                    Log.i(TAG, response.toString())

                    // Application code
                    try {
                        val jsonObject: JSONObject = `object`
                        var user = User()
                        user.socialType = (User.socialType_Facebook)
                        if (!jsonObject.isNull("email"))
                            user.email = jsonObject.getString("email")
                        if (!jsonObject.isNull("name"))
                            user.name = jsonObject.getString("name")
                        if (!jsonObject.isNull("id"))
                            user.id = jsonObject.getString("id")
                        if (imageUrl != null)
                            user.fullImagePath = imageUrl.toString()

                        user.token = accessToken!!.token
//                        logInSocial(user.token, AppConstants.FACEBOOK)
                      Toast.makeText(applicationContext,"Successfully login",Toast.LENGTH_SHORT).show()
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
                hideDialogue()
                // App code
                Log.e(TAG, "Facebook onCancel")
            }

            override fun onError(exception: FacebookException) {
                hideDialogue()
                Log.e(TAG, exception.toString())
                showAlertDialouge(exception.toString())
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
            hideDialogue()
            // Signed in successfully, show authenticated UI.
            val acct = result.signInAccount

            val personName = acct!!.displayName

            Log.i(TAG, "Google info " + personName + " " + acct.email + " " + acct.id + " " + acct.photoUrl)
            val user = User()
            user.socialType = User.Companion.socialType_Google
            user.email = acct.email!!
            user.token = acct.idToken!!
            user.name = personName!!
            if (acct.photoUrl != null)
                user.fullImagePath = acct.photoUrl!!.toString()
            user.id = acct.id!!
//            logInSocial(user.token, AppConstants.GOOGLE)
            Toast.makeText(applicationContext,"Successfully login",Toast.LENGTH_SHORT).show()
        } else if (!result.isSuccess) {
            hideDialogue()
            Log.e(TAG, "google failed")
            showAlertDialouge("google failed")
        }
    }

    //endregion


}
