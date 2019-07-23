package com.twoam.cartello.View

import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.LayerDrawable

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.twoam.Networking.INetworkCallBack
import com.twoam.Networking.NetworkManager
import com.twoam.cartello.Model.Address
import com.twoam.cartello.R
import com.twoam.cartello.Utilities.API.ApiResponse
import com.twoam.cartello.Utilities.API.ApiServices
import com.twoam.cartello.Utilities.Adapters.AddressAdapter
import com.twoam.cartello.Utilities.Base.BaseDefaultActivity
import com.twoam.cartello.Utilities.DB.PreferenceController
import com.twoam.cartello.Utilities.General.AppConstants
import com.twoam.cartello.Utilities.General.IBottomSheetCallback
import kotlinx.android.synthetic.main.activity_profile.*
import android.support.v4.view.ViewCompat.getMinimumHeight
import android.graphics.drawable.Drawable
import android.view.WindowManager


class ProfileActivity : BaseDefaultActivity(), View.OnClickListener, IBottomSheetCallback {


    //region Members
    var addressList = ArrayList<Address>()
    var addressId: String? = null
    var removedAddressIndex = 0
    //endregion

    //region Events
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        init()

        //CHANGE THE RATING BAR COLOR
        changeRateBarSettings()
        getUserProfileData()
    }


    override fun onClick(v: View?) {
        when (v?.id) {

            R.id.ivBackProfile -> {
                finish()
            }
            R.id.tvAddAddress -> {
                startActivity(Intent(this, CreateAddressActivity::class.java))
                finish()
            }
            R.id.tvEditProfile -> {
                startActivity(Intent(this, EditAddressProfileActivity::class.java))
                finish()
            }
        }
    }

    override fun onBottomSheetClosed(isClosed: Boolean) {

    }

    override fun onBottomSheetSelectedItem(index: Int) {
        if (index > 0)//delete address
        {
            removedAddressIndex = index
            removeAddress(removedAddressIndex)

        }

    }

    //endregion

    //region Helper Functions
    private fun init() {

        ivBackProfile.setOnClickListener(this)
        tvAddAddress.setOnClickListener(this)
        tvEditProfile.setOnClickListener(this)
        if (intent.hasExtra("addressId")) {
            addressId = intent.getStringExtra("addressId")
//            addressList.

        }

    }

    private fun changeRateBarSettings() {
    //        val stars = rbRate.progressDrawable as LayerDrawable
    //        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP)
        //val starDrawable = resources.getDrawable(R.drawable.YOUR_IMAGE)
//        val height = 24
//        val params = rbRate.getLayoutParams() as WindowManager.LayoutParams
//        params.height = height
//        rbRate.setLayoutParams(params)
    }

    private fun getUserProfileData() {
        if (AppConstants.CurrentLoginUser.addresses != null &&
                AppConstants.CurrentLoginUser.addresses!!.size > 0) {

            var user = AppConstants.CurrentLoginUser
            addressList = AppConstants.CurrentLoginUser.addresses!!

            tvProfileName.text = user.name
            tvTelNo.text = user.phone
            tvEmailValue.text = user.email
            tvBirthDateValue.text = user.birthdate

            var adapter = AddressAdapter(this@ProfileActivity, addressList)
            rvAddress.adapter = adapter
            rvAddress.layoutManager = LinearLayoutManager(this)
        }

    }

    private fun removeAddress(removedAddressID: Int): Boolean {
        var done = false

        if (NetworkManager().isNetworkAvailable(this@ProfileActivity)) {
            var request = NetworkManager().create(ApiServices::class.java)
            var token = AppConstants.BEARER + AppConstants.CurrentLoginUser.token
            var endpoint = request.removeAddress(token, removedAddressID)
            NetworkManager().request(endpoint, object : INetworkCallBack<ApiResponse<Boolean>> {
                override fun onFailed(error: String) {
                    hideDialogue()
                    showAlertDialouge(error)
                }

                override fun onSuccess(response: ApiResponse<Boolean>) {
                    if (response.code == AppConstants.CODE_200) {
                        //saved the address in shared prefrences
                        hideDialogue()
                        addressList.removeAt(removedAddressIndex) //remove address form list
                        AppConstants.CurrentLoginUser.addresses = addressList
                        //todo save address to the shared prefrences
//                        PreferenceController.getInstance(this@ProfileActivity).setAddressPref(AppConstants.ADDRESS,AppConstants.CurrentLoginUser.addresses!!)
                        //refresh adapter
                        var adapter = AddressAdapter(this@ProfileActivity, addressList)
                        rvAddress.adapter = adapter
                        rvAddress.layoutManager = LinearLayoutManager(this@ProfileActivity)


                    }
                }
            })

        } else {
            hideDialogue()
            showAlertDialouge(getString(R.string.error_no_internet))
        }

        return done
    }
    //endregion


}
