package com.twoam.cartello.View

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import bolts.Bolts
import com.bumptech.glide.Glide.init
import com.twoam.Networking.INetworkCallBack
import com.twoam.Networking.NetworkManager
import com.twoam.cartello.Model.Address
import com.twoam.cartello.Model.Area
import com.twoam.cartello.Model.City
import com.twoam.cartello.Model.User
import com.twoam.cartello.R
import com.twoam.cartello.R.string.name
import com.twoam.cartello.Utilities.API.ApiResponse
import com.twoam.cartello.Utilities.API.ApiServices
import com.twoam.cartello.Utilities.Adapters.AreaAdapter
import com.twoam.cartello.Utilities.Adapters.CityAdapter
import com.twoam.cartello.Utilities.Base.BaseDefaultActivity
import com.twoam.cartello.Utilities.DB.PreferenceController
import com.twoam.cartello.Utilities.General.AppConstants
import com.twoam.cartello.Utilities.General.CloseBottomSheetDialog
import com.twoam.cartello.Utilities.General.IBottomSheetCallback
import kotlinx.android.synthetic.main.activity_edit_delete_address.*

class EditDeleteAddressActivity : BaseDefaultActivity(), View.OnClickListener, IBottomSheetCallback {


    //region Members

    private var currentLoginUser: User = User()
    private lateinit var cities: ArrayList<City>
    private lateinit var dummyCities: ArrayList<City>
    private lateinit var areas: ArrayList<Area>
    private lateinit var dummyAreas: ArrayList<Area>
    private lateinit var selectedCity: City
    private lateinit var selectedArea: Area
    private lateinit var etCity: AutoCompleteTextView
    private lateinit var etArea: AutoCompleteTextView
    private var newAddress: Address = Address()
    //    private var addressIdIndex: Int? = null
    private var bottomSheet = CloseBottomSheetDialog()

    //endregion

    //region Events
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_delete_address)

        showDialogue()
        init()
        prepareData()


    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ivBackEditDeleteAddress -> {
                finish()
            }
            R.id.tvDelete -> {
                bottomSheet.Action = 1
                bottomSheet.show(supportFragmentManager, "Custom Bottom Sheet")
            }
            R.id.btnUpdate -> {
                var name = etFullName.text.toString()
                var city = etCity.text.toString()
                var area = etArea.text.toString()
                var address = etAddress.text.toString()
                var apt = etApt.text.toString()
                var floor = etFloor.text.toString()
                var landMark = etLandMark.text.toString()
                var valid = validateUserData(name, city, area, address, apt, floor)
                if (valid) {
                    showDialogue()
                    addAddress(name, selectedCity.id.toString(), selectedArea.id.toString(), address, apt, floor, landMark)
                }

            }
            R.id.ivBack -> {
                startActivity(Intent(this@EditDeleteAddressActivity, ProfileActivity::class.java))
                finish()
            }


        }
    }

    override fun onBottomSheetClosed(isClosed: Boolean) {

    }

    override fun onBottomSheetSelectedItem(index: Int) {
        if (index >= 0) {
            var address = AppConstants.CurrentLoginUser.addresses?.get(index)
            removeAddress(address?.id.toString())
        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
//        startActivity(Intent(this@EditDeleteAddressActivity, ProfileActivity::class.java))
        finish()
    }


    //endregion

    //region Helper Functions

    private fun prepareData(): ArrayList<City> {

        if (NetworkManager().isNetworkAvailable(this)) {

            var request = NetworkManager().create(ApiServices::class.java)
            var endPoint = request.getCities()
            NetworkManager().request(endPoint, object : INetworkCallBack<ApiResponse<ArrayList<City>>> {
                override fun onFailed(error: String) {
                    hideDialogue()
                    showAlertDialouge(error)
                }

                override fun onSuccess(response: ApiResponse<ArrayList<City>>) {
                    if (response.code == AppConstants.CODE_200) {
                        cities = response.data!!
                        prepareCities(cities)
                        hideDialogue()

                    } else {
                        hideDialogue()
                        Toast.makeText(applicationContext, response.message, Toast.LENGTH_SHORT).show()
                    }
                }
            })
        } else {
            hideDialogue()
            showAlertDialouge(getString(R.string.error_no_internet))
        }

        return cities
    }

    private fun prepareCities(citiesData: ArrayList<City>) {
        cities = getCityData(citiesData)
        var arrayAdapter = CityAdapter(
                this@EditDeleteAddressActivity, android.R.layout.simple_list_item_1, cities)
        etCity.setAdapter(arrayAdapter)
        etCity.isCursorVisible = false

        loadSavedAddress()

        etCity.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            etCity.showDropDown()
            var city = parent.getItemAtPosition(position) as City
            //areas


            if (city.id > 0) {
                selectedCity = city
                prepareAreas(city)

                selectedArea = Area(0, getString(R.string.selectArea))
                etArea.setText(getString(R.string.selectArea))
            } else {
                selectedCity = City(0, getString(R.string.selectCity))
                selectedArea = Area(0, getString(R.string.selectArea))
                etArea.setAdapter(null)
                etArea.setText(getString(R.string.selectArea))
            }
        }

        etCity.setOnClickListener(View.OnClickListener {
            etCity.showDropDown()

        })
    }

    private fun loadSavedAddress() {
        if (intent.hasExtra("addressIdPosition")) {
            val bundle: Bundle? = intent.extras
            var addressIndex = bundle?.get("addressIdPosition") as Int
            var address = AppConstants.CurrentLoginUser.addresses?.get(addressIndex)
            var city = cities.find { it.id == address!!.city_id }

            prepareAreas(city!!)

            var area = areas.find { it.id == address!!.area_id }
            selectedCity = city!!
            selectedArea = area!!

            etFullName.setText(address?.name)
            etCity.setText(cities.find { it.id == address!!.city_id }?.name)
            etArea.setText(areas.find { it.id == address!!.area_id }?.name)
            etAddress.setText(address?.address)
            etApt.setText(address?.apartment)
            etFloor.setText(address?.floor)
            etLandMark.setText(address?.landmark)

        }
    }

    private fun prepareAreas(city: City) {
        areas = getAreaData(city)

        var arrayAdapter = AreaAdapter(
                this@EditDeleteAddressActivity, android.R.layout.simple_list_item_1, areas)
        etArea.setAdapter(arrayAdapter)
        etArea.isCursorVisible = false



        etArea.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            etArea.showDropDown()
            var area = parent.getItemAtPosition(position) as Area
            if (area.id > 0)
                selectedArea = area
            else
                selectedArea = Area(0, getString(R.string.selectArea))
        }

        etArea.setOnClickListener({
            etArea.showDropDown()

        })
    }

    private fun addAddress(name: String, city: String, area: String, address: String, apt: String, floor: String, landMark: String): Address {

        if (NetworkManager().isNetworkAvailable(this)) {
            var request = NetworkManager().create(ApiServices::class.java)
            var authorization = AppConstants.BEARER + currentLoginUser.token
            var endPoint = request.addAddress(authorization, name, city, area, address, apt, floor, landMark)
            NetworkManager().request(endPoint, object : INetworkCallBack<ApiResponse<Address>> {
                override fun onFailed(error: String) {
                    hideDialogue()
                    showAlertDialouge(error)
                }

                override fun onSuccess(response: ApiResponse<Address>) {
                    if (response.code == AppConstants.CODE_200) {
                        newAddress = response.data!!
                        PreferenceController.getInstance(this@EditDeleteAddressActivity).setAddressPref(AppConstants.ADDRESS, newAddress)
                        PreferenceController.getInstance(this@EditDeleteAddressActivity).Set(AppConstants.HASADDRESS, AppConstants.TRUE)
                        hideDialogue()
                        finish()
                        startActivity(Intent(this@EditDeleteAddressActivity, MainActivity::class.java))

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
        return newAddress!!
    }

    private fun removeAddress(addressId: String): Boolean {
        var done = false
        if (NetworkManager().isNetworkAvailable(this)) {
            var request = NetworkManager().create(ApiServices::class.java)
            var authorization = AppConstants.BEARER + currentLoginUser.token
            var endPoint = request.removeAddress(authorization)
            NetworkManager().request(endPoint, object : INetworkCallBack<ApiResponse<Boolean>> {
                override fun onFailed(error: String) {
                    hideDialogue()
                    showAlertDialouge(error)
                }

                override fun onSuccess(response: ApiResponse<Boolean>) {
                    if (response.code == AppConstants.CODE_200) {
                        done = response.data!!
                        hideDialogue()

                        //todo get the address list and remove the current deleted address from it and save it  and navigate toProfile activity
                        //and refresh adapter with the new list
                        PreferenceController.getInstance(this@EditDeleteAddressActivity).setAddressPref(AppConstants.ADDRESS, newAddress)
//                        PreferenceController.getInstance(this@EditDeleteAddressActivity).Set(AppConstants.HASADDRESS, AppConstants.TRUE)
                        startActivity(Intent(this@EditDeleteAddressActivity, ProfileActivity::class.java).putExtra("addressId", addressId))
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
        return done!!
    }

    private fun init() {
        cities = ArrayList()
        areas = ArrayList()
        selectedCity = City(0, getString(R.string.selectCity))
        selectedArea = Area(0, getString(R.string.selectArea))

        etCity = findViewById(R.id.etCity)
        etArea = findViewById(R.id.etArea)
        btnUpdate.setOnClickListener(this)
        ivBackEditDeleteAddress.setOnClickListener(this)
        tvDelete.setOnClickListener(this)
        currentLoginUser = PreferenceController.getInstance(this).getUserPref(AppConstants.USER_DATA)!!


    }

    private fun validateUserData(fullName: String, city: String, area: String, address: String, apt: String, floor: String): Boolean {
        var valid = false

        if (fullName.isNullOrEmpty()) {
            tvFullNameError.visibility = View.VISIBLE
            tvCityError.visibility = View.INVISIBLE
            tvAreaError.visibility = View.INVISIBLE
            tvAddressError.visibility = View.INVISIBLE
            tvAptError.visibility = View.INVISIBLE
            tvFloorError.visibility = View.INVISIBLE
            valid = false
        } else if (selectedCity.id == 0) {

            tvFullNameError.visibility = View.INVISIBLE
            tvCityError.visibility = View.VISIBLE
            tvAreaError.visibility = View.INVISIBLE
            tvAddressError.visibility = View.INVISIBLE
            tvAptError.visibility = View.INVISIBLE
            tvFloorError.visibility = View.INVISIBLE
            valid = false
        } else if (selectedArea.id == 0) {
            tvFullNameError.visibility = View.INVISIBLE
            tvCityError.visibility = View.INVISIBLE
            tvAreaError.visibility = View.VISIBLE
            tvAddressError.visibility = View.INVISIBLE
            tvAptError.visibility = View.INVISIBLE
            tvFloorError.visibility = View.INVISIBLE
            valid = false
        } else if (address.isNullOrEmpty()) {
            tvFullNameError.visibility = View.INVISIBLE
            tvCityError.visibility = View.INVISIBLE
            tvAreaError.visibility = View.INVISIBLE
            tvAddressError.visibility = View.VISIBLE
            tvAptError.visibility = View.INVISIBLE
            tvFloorError.visibility = View.INVISIBLE
            valid = false
        } else if (apt.isNullOrEmpty()) {
            tvFullNameError.visibility = View.INVISIBLE
            tvCityError.visibility = View.INVISIBLE
            tvAreaError.visibility = View.INVISIBLE
            tvAddressError.visibility = View.INVISIBLE
            tvAptError.visibility = View.VISIBLE
            tvFloorError.visibility = View.INVISIBLE
            valid = false
        } else if (floor.isNullOrEmpty()) {
            tvFullNameError.visibility = View.INVISIBLE
            tvCityError.visibility = View.INVISIBLE
            tvAreaError.visibility = View.INVISIBLE
            tvAddressError.visibility = View.INVISIBLE
            tvAptError.visibility = View.INVISIBLE
            tvFloorError.visibility = View.VISIBLE
            valid = false
        } else {
            tvFullNameError.visibility = View.INVISIBLE
            tvCityError.visibility = View.INVISIBLE
            tvAreaError.visibility = View.INVISIBLE
            tvAddressError.visibility = View.INVISIBLE
            tvAptError.visibility = View.INVISIBLE
            tvFloorError.visibility = View.INVISIBLE
            valid = true
        }
        return valid
    }

    private fun getCityData(cities: ArrayList<City>): ArrayList<City> {
        if (cities.size > 0) {

            var newArray = cities
            dummyCities = ArrayList()

            var firstItem = City(0, getString(R.string.selectCity))
            dummyCities.add(firstItem)

            for (c in newArray.indices) {
                dummyCities.add(newArray[c])
            }
        }
        return dummyCities
    }

    private fun getAreaData(city: City): ArrayList<Area> {
        if (city.id > 0) {

            var newArray = city.areas
            dummyAreas = ArrayList()

            var firstItem = Area(0, getString(R.string.selectArea))
            dummyAreas.add(firstItem)

            for (c in newArray!!.indices) {
                dummyAreas.add(newArray[c])
            }
        }

        return dummyAreas
    }
    //endregion
}
