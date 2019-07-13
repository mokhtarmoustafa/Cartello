package com.twoam.cartello.View


import android.content.Intent
import android.os.Bundle
import android.view.View
import com.twoam.Networking.INetworkCallBack
import com.twoam.Networking.NetworkManager
import com.twoam.cartello.Model.User
import com.twoam.cartello.R
import com.twoam.cartello.Utilities.API.ApiResponse
import com.twoam.cartello.Utilities.API.ApiServices
import com.twoam.cartello.Utilities.Base.BaseDefaultActivity
import com.twoam.cartello.Utilities.General.AppConstants
import com.twoam.cartello.Utilities.General.CustomEditTextDatePicker
import kotlinx.android.synthetic.main.activity_edit_address_profile.*


class EditAddressProfileActivity : BaseDefaultActivity(), View.OnClickListener {


    //region Members
    private lateinit var birthDate: CustomEditTextDatePicker
    private var currentLoginUser: User = User()

    //endregion

    //region Events
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_address_profile)

        init()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ivBackEditAddress -> {
                startActivity(Intent(this, ProfileActivity::class.java))
                finish()
            }
            R.id.btnSaveEditProfile -> {
                var userName = etUserNameEditProfile.text.toString()
                var phoneNo = etPhoneNumberEditProfile.text.toString()
                var birthDate = etDateOfBirthEditProfile.text.toString()

                var isValid = validateUserData(userName, phoneNo, birthDate)

                if (isValid) {
                    editProfile(userName, phoneNo, birthDate)
                }
            }


        }
    }


    //endregion

    //region Helper Functions
    private fun init() {
        ivBackEditAddress.setOnClickListener(this)
        btnSaveEditProfile.setOnClickListener(this)

        birthDate = CustomEditTextDatePicker(this, etDateOfBirthEditProfile.id)
        currentLoginUser = AppConstants.CurrentLoginUser

        etEmailEditProfile.setText(currentLoginUser.email)
        etUserNameEditProfile.setText(currentLoginUser.name)
        etPhoneNumberEditProfile.setText(currentLoginUser.phone)
        etDateOfBirthEditProfile.setText(currentLoginUser.birthdate)


    }

    private fun validateUserData(userName: String, phoneNo: String, birthDate: String): Boolean {
        var valid = false

        if (userName.isNullOrEmpty()) {
            tvUserNameErrorEditProfile.visibility = View.VISIBLE
            tvPhoneNumberErrorEditProfile.visibility = View.INVISIBLE
            tvDateOfBirthErrorEditProfile.visibility = View.INVISIBLE
            valid = false
        } else if (phoneNo.isNullOrEmpty()) {
            tvPhoneNumberErrorEditProfile.visibility = View.VISIBLE
            tvUserNameErrorEditProfile.visibility = View.INVISIBLE
            tvDateOfBirthErrorEditProfile.visibility = View.INVISIBLE
            valid = false
        } else if (birthDate.isNullOrEmpty()) {
            tvDateOfBirthErrorEditProfile.visibility = View.VISIBLE
            tvPhoneNumberErrorEditProfile.visibility = View.INVISIBLE
            tvUserNameErrorEditProfile.visibility = View.INVISIBLE

            valid = false
        } else {
            tvPhoneNumberErrorEditProfile.visibility = View.INVISIBLE
            tvUserNameErrorEditProfile.visibility = View.INVISIBLE
            tvDateOfBirthErrorEditProfile.visibility = View.INVISIBLE
            valid = true
        }
        return valid
    }

    private fun editProfile(userName: String, phoneNo: String, birthDate: String) {

        if (NetworkManager().isNetworkAvailable(this)) {
            var authorization = AppConstants.BEARER + currentLoginUser.token
            var request = NetworkManager().create(ApiServices::class.java)
            var endPoint = request.editProfile(authorization, userName, phoneNo, birthDate)

            NetworkManager().request(endPoint, object : INetworkCallBack<ApiResponse<User>> {
                override fun onFailed(error: String) {
                    hideDialogue()
                    showAlertDialouge(error)
                }

                override fun onSuccess(response: ApiResponse<User>) {
                    if (response.code == AppConstants.CODE_200) {
                        currentLoginUser = response.data!!
                        saveUserData(currentLoginUser)
                        startActivity(Intent(this@EditAddressProfileActivity, ProfileActivity::class.java))
                        finish()
                    } else {
                        hideDialogue()
                        showAlertDialouge(response.message)
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
