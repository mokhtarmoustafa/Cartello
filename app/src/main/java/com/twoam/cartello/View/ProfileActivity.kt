package com.twoam.cartello.View

import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.LayerDrawable

import android.os.Bundle
import android.view.View
import com.twoam.cartello.Model.Address
import com.twoam.cartello.R
import com.twoam.cartello.Utilities.Adapters.AddressAdapter
import com.twoam.cartello.Utilities.Base.BaseDefaultActivity
import com.twoam.cartello.Utilities.General.AppConstants
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : BaseDefaultActivity(),View.OnClickListener {


    //region Members
    var addressList = ArrayList<Address>()
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
      when(v?.id)
      {

          R.id.ivBackProfile ->
          {
              finish()
          }
          R.id.tvAddAddress ->
          {
              startActivity(Intent(this, CreateAddressActivity::class.java))
              finish()
          }
          R.id.tvEditProfile ->
          {
              startActivity(Intent(this, EditAddressProfileActivity::class.java))
              finish()
          }
      }
    }

    //endregion

    //region Helper Functions
    private fun init() {

        ivBackProfile.setOnClickListener(this)
        tvAddAddress.setOnClickListener(this)
        tvEditProfile.setOnClickListener(this)


    }

    private fun changeRateBarSettings() {
        val stars = rbRate.progressDrawable as LayerDrawable
        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP)
    }

    private fun getUserProfileData() {
        if (AppConstants.CurrentLoginUser.addresses != null &&
                AppConstants.CurrentLoginUser.addresses!!.size > 0) {

            var user=AppConstants.CurrentLoginUser
            addressList = AppConstants.CurrentLoginUser.addresses!!

            tvProfileName.text = user.name
            tvTelNo.text= user.phone
            tvEmailValue.text=user.email
            tvBirthDateValue.text=user.birthdate


            rvAddress.adapter = AddressAdapter(this, addressList)
        }

    }
    //endregion


}
