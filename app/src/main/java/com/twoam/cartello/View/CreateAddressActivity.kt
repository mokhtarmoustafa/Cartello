package com.twoam.cartello.View

import android.os.Bundle
import android.view.View
import android.widget.*
import com.twoam.Networking.INetworkCallBack
import com.twoam.Networking.NetworkManager
import com.twoam.cartello.Model.Address
import com.twoam.cartello.Model.Area
import com.twoam.cartello.Model.City
import com.twoam.cartello.R
import com.twoam.cartello.Utilities.API.ApiResponse
import com.twoam.cartello.Utilities.API.ApiServices
import com.twoam.cartello.Utilities.General.AppConstants
import android.widget.Toast
import android.widget.AdapterView
import com.twoam.cartello.Utilities.Adapters.AreaAdapter
import com.twoam.cartello.Utilities.Adapters.CityAdapter
import android.content.Intent
import com.twoam.cartello.Model.User
import com.twoam.cartello.Utilities.Base.BaseDefaultActivity
import com.twoam.cartello.Utilities.DB.PreferenceController


class CreateAddressActivity : BaseDefaultActivity(), View.OnClickListener {


    //region Members

    private var currentLoginUser: User = User()
    private lateinit var cities: ArrayList<City>
    private lateinit var dummyCities: ArrayList<City>
    private lateinit var areas: ArrayList<Area>
    private lateinit var dummyAreas: ArrayList<Area>
    private lateinit var selectedCity: City
    private lateinit var selectedArea: Area
    private lateinit var ivBack: ImageView
    private lateinit var etName: EditText
    private lateinit var etCity: AutoCompleteTextView
    private lateinit var etArea: AutoCompleteTextView
    private lateinit var etAddress: EditText
    private lateinit var etApt: EditText
    private lateinit var etFloor: EditText
    private lateinit var etLandMark: EditText
    private lateinit var btnAdd: Button

    private lateinit var tvErrorName: TextView
    private lateinit var tvErrorCity: TextView
    private lateinit var tvErrorArea: TextView

    private lateinit var tvErrorAddress: TextView
    private lateinit var tvErrorApt: TextView
    private lateinit var tvErrorFloor: TextView
    private var newAddress: Address = Address()

    //endregion

    //region Events
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_address)

        init()
        prepareData()

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnAdd -> {
                var name = etName.text.toString()
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
              startActivity(Intent(this@CreateAddressActivity, ProfileActivity::class.java))
                finish()
            }


        }
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

                    } else {
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
                this@CreateAddressActivity, android.R.layout.simple_list_item_1, cities)
        etCity.setAdapter(arrayAdapter)
        etCity.isCursorVisible = false

        etCity.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            etCity.showDropDown()
            var city = parent.getItemAtPosition(position) as City
            //areas
            if (city.id > 0) {
                selectedCity = city
                prepareAreas(city)
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

    private fun prepareAreas(city: City) {
        areas = getAreaData(city)

        var arrayAdapter = AreaAdapter(
                this@CreateAddressActivity, android.R.layout.simple_list_item_1, areas)
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
                        hideDialogue()
                        PreferenceController.getInstance(this@CreateAddressActivity).setAddressPref(AppConstants.ADDRESS, newAddress)
                        PreferenceController.getInstance(this@CreateAddressActivity).Set(AppConstants.HASADDRESS, AppConstants.TRUE)
                        startActivity(Intent(this@CreateAddressActivity, MainActivity::class.java))
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
        return newAddress!!
    }

    private fun init() {
        cities = ArrayList()
        areas = ArrayList()
        selectedCity = City(0, getString(R.string.selectCity))
        selectedArea = Area(0, getString(R.string.selectArea))

        ivBack = findViewById(R.id.ivBack)
        etName = findViewById(R.id.etFullName)

        etCity = findViewById(R.id.etCity)
        etArea = findViewById(R.id.etArea)
        etAddress = findViewById(R.id.etAddress)
        etApt = findViewById(R.id.etApt)
        etFloor = findViewById(R.id.etFloor)
        etLandMark = findViewById(R.id.etLandMArk)

        tvErrorName = findViewById(R.id.tvFullNameError)
        tvErrorCity = findViewById(R.id.tvCityError)
        tvErrorArea = findViewById(R.id.tvAreaError)

        tvErrorAddress = findViewById(R.id.tvAddressError)
        tvErrorApt = findViewById(R.id.tvAptError)
        tvErrorFloor = findViewById(R.id.tvFloorError)

        btnAdd = findViewById(R.id.btnAdd)
        btnAdd.setOnClickListener(this)
        ivBack.setOnClickListener(this)

        currentLoginUser = PreferenceController.getInstance(this).getUserPref(AppConstants.USER_DATA)!!

    }

    private fun validateUserData(fullName: String, city: String, area: String, address: String, apt: String, floor: String): Boolean {
        var valid = false

        if (fullName.isNullOrEmpty()) {
            tvErrorName.visibility = View.VISIBLE
            tvErrorCity.visibility = View.INVISIBLE
            tvErrorArea.visibility = View.INVISIBLE
            tvErrorAddress.visibility = View.INVISIBLE
            tvErrorApt.visibility = View.INVISIBLE
            tvErrorFloor.visibility = View.INVISIBLE
            valid = false
        } else if (selectedCity.id == 0) {

            tvErrorName.visibility = View.INVISIBLE
            tvErrorCity.visibility = View.VISIBLE
            tvErrorArea.visibility = View.INVISIBLE
            tvErrorAddress.visibility = View.INVISIBLE
            tvErrorApt.visibility = View.INVISIBLE
            tvErrorFloor.visibility = View.INVISIBLE
            valid = false
        } else if (selectedArea.id == 0) {
            tvErrorName.visibility = View.INVISIBLE
            tvErrorCity.visibility = View.INVISIBLE
            tvErrorArea.visibility = View.VISIBLE
            tvErrorAddress.visibility = View.INVISIBLE
            tvErrorApt.visibility = View.INVISIBLE
            tvErrorFloor.visibility = View.INVISIBLE
            valid = false
        } else if (address.isNullOrEmpty()) {
            tvErrorName.visibility = View.INVISIBLE
            tvErrorCity.visibility = View.INVISIBLE
            tvErrorArea.visibility = View.INVISIBLE
            tvErrorAddress.visibility = View.VISIBLE
            tvErrorApt.visibility = View.INVISIBLE
            tvErrorFloor.visibility = View.INVISIBLE
            valid = false
        } else if (apt.isNullOrEmpty()) {
            tvErrorName.visibility = View.INVISIBLE
            tvErrorCity.visibility = View.INVISIBLE
            tvErrorArea.visibility = View.INVISIBLE
            tvErrorAddress.visibility = View.INVISIBLE
            tvErrorApt.visibility = View.VISIBLE
            tvErrorFloor.visibility = View.INVISIBLE
            valid = false
        } else if (floor.isNullOrEmpty()) {
            tvErrorName.visibility = View.INVISIBLE
            tvErrorCity.visibility = View.INVISIBLE
            tvErrorArea.visibility = View.INVISIBLE
            tvErrorAddress.visibility = View.INVISIBLE
            tvErrorApt.visibility = View.INVISIBLE
            tvErrorFloor.visibility = View.VISIBLE
            valid = false
        } else {
            tvErrorName.visibility = View.INVISIBLE
            tvErrorCity.visibility = View.INVISIBLE
            tvErrorArea.visibility = View.INVISIBLE
            tvErrorAddress.visibility = View.INVISIBLE
            tvErrorApt.visibility = View.INVISIBLE
            tvErrorFloor.visibility = View.INVISIBLE
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
