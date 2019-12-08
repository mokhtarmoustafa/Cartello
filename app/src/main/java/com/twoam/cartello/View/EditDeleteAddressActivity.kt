package com.twoam.cartello.View

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import com.twoam.Networking.INetworkCallBack
import com.twoam.Networking.NetworkManager
import com.twoam.cartello.Model.*
import com.twoam.cartello.R
import com.twoam.cartello.Utilities.API.ApiResponse
import com.twoam.cartello.Utilities.API.ApiServices
import com.twoam.cartello.Utilities.Adapters.AreaAdapter
import com.twoam.cartello.Utilities.Adapters.CityAdapter
import com.twoam.cartello.Utilities.Base.BaseDefaultActivity
import com.twoam.cartello.Utilities.DB.PreferenceController
import com.twoam.cartello.Utilities.General.AppConstants
import com.twoam.cartello.Utilities.General.CloseBottomSheetDialog
import com.twoam.cartello.Utilities.Interfaces.IBottomSheetCallback
import kotlinx.android.synthetic.main.activity_edit_delete_address.*

class EditDeleteAddressActivity : BaseDefaultActivity(), View.OnClickListener, IBottomSheetCallback {


    //region Members

    private var currentLoginUser: User = User()
    private var currentAddress: Addresses = Addresses()
    private var cities = ArrayList<City>()
    private var dummyCities = ArrayList<City>()
    private var areas = ArrayList<Area>()
    private var dummyAreas = ArrayList<Area>()
    private var selectedCity = City()
    private var selectedArea = Area()
    private lateinit var etCity: AutoCompleteTextView
    private lateinit var etArea: AutoCompleteTextView
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
                    AppConstants.CurrentSelectedAddresses.name = name
                    AppConstants.CurrentSelectedAddresses.city_id = selectedCity.id
                    AppConstants.CurrentSelectedAddresses.city.name = city
                    AppConstants.CurrentSelectedAddresses.area_id = selectedArea.id
                    AppConstants.CurrentSelectedAddresses.area.name = area
                    AppConstants.CurrentSelectedAddresses.address = address
                    AppConstants.CurrentSelectedAddresses.apartment = apt
                    AppConstants.CurrentSelectedAddresses.floor = floor
                    AppConstants.CurrentSelectedAddresses.landmark = landMark

                    updateAddress(currentAddress.id, name, selectedCity.id.toString(), selectedArea.id.toString(), address, apt, floor, landMark)
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
        if (index == 1) {//delete


            currentAddress = AppConstants.CurrentSelectedAddresses!!
            showDialogue()
            removeAddress(currentAddress.id!!)
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
                        PreferenceController.instance?.setCitiesPref(AppConstants.CITIES_DATA, cities)
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
        if (AppConstants.CurrentSelectedAddresses != null) {

            currentAddress = AppConstants.CurrentSelectedAddresses
            var city = cities.find { it.id == currentAddress!!.city_id }

            prepareAreas(city!!)

            var area = areas.find { it.id == currentAddress!!.area_id }
            selectedCity = city!!
            selectedArea = area!!

            etFullName.setText(currentAddress?.name)
            etCity.setText(cities.find { it.id == currentAddress!!.city_id }?.name)
            etArea.setText(areas.find { it.id == currentAddress!!.area_id }?.name)
            etAddress.setText(currentAddress?.address)
            etApt.setText(currentAddress?.apartment)
            etFloor.setText(currentAddress?.floor)
            etLandMark.setText(currentAddress?.landmark)

        }


    }

    private fun prepareAreas(city: City) {
        areas = getAreaData(city)

        var arrayAdapter = AreaAdapter(
                this@EditDeleteAddressActivity, android.R.layout.simple_list_item_1, areas)
        etArea.setAdapter(arrayAdapter)
        etArea.isCursorVisible = false
        etArea.isCursorVisible = false



        etArea.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            etArea.showDropDown()
            var area = parent.getItemAtPosition(position) as Area
            if (area.id > 0)
                selectedArea = area
            else
                selectedArea = Area(0, getString(R.string.selectArea))
        }

        etArea.setOnClickListener {
            etArea.showDropDown()

        }
    }

    private fun updateAddress(addressId: Int, name: String, city: String, area: String, address: String, apt: String, floor: String, landMark: String): Addresses {

        if (NetworkManager().isNetworkAvailable(this)) {
            var request = NetworkManager().create(ApiServices::class.java)
            var authorization = AppConstants.BEARER + currentLoginUser.token
            var endPoint = request.updateAddress(authorization, addressId, name, city, area, address, apt, floor, landMark)
            NetworkManager().request(endPoint, object : INetworkCallBack<ApiResponse<Addresses>> {
                override fun onFailed(error: String) {
                    hideDialogue()
                    showAlertDialouge(error)
                }

                override fun onSuccess(response: ApiResponse<Addresses>) {
                    if (response.code == AppConstants.CODE_200) {
//                        currentAddress = response.data!!

                        var oldAddressIndex = AppConstants.CurrentLoginUser.addresses?.indexOf(AppConstants.CurrentLoginUser.addresses?.find { it.id == addressId }!!)

                        AppConstants.CurrentLoginUser.addresses?.set(oldAddressIndex!!, AppConstants.CurrentSelectedAddresses)

                        PreferenceController.getInstance(applicationContext).setUserPref(AppConstants.USER_DATA, AppConstants.CurrentLoginUser)
                        hideDialogue()
                        finish()
                        startActivity(Intent(this@EditDeleteAddressActivity, ProfileActivity::class.java))

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
        return currentAddress
    }

    private fun removeAddress(addressId: Int): Boolean {
        var done = false
        if (NetworkManager().isNetworkAvailable(this)) {
            var request = NetworkManager().create(ApiServices::class.java)
            var authorization = AppConstants.BEARER + currentLoginUser.token
            var endPoint = request.removeAddress(addressId, authorization)
            NetworkManager().request(endPoint, object : INetworkCallBack<ApiResponse<Addresses>> {
                override fun onFailed(error: String) {
                    hideDialogue()
                    showAlertDialouge(error)
                }

                override fun onSuccess(response: ApiResponse<Addresses>) {
                    if (response.code == AppConstants.CODE_200) {
                        var oldAddress = AppConstants.CurrentLoginUser.addresses!!.find { it.id == addressId }
                        AppConstants.CurrentLoginUser.addresses!!.remove(oldAddress)
                        AppConstants.CurrentLoginUser.hasAddress = AppConstants.CurrentLoginUser.addresses.size != 0
                        PreferenceController.getInstance(applicationContext).setUserPref(AppConstants.USER_DATA, AppConstants.CurrentLoginUser)
                        hideDialogue()
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
        currentLoginUser = PreferenceController.getInstance(applicationContext).getUserPref(AppConstants.USER_DATA)!!


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
