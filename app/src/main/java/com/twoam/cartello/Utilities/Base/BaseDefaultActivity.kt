package com.twoam.cartello.Utilities.Base

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.AppCompatDelegate
import android.view.Menu
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.RelativeLayout
import com.twoam.cartello.Model.City
import com.twoam.cartello.Model.User
import com.twoam.cartello.R
import com.twoam.cartello.Utilities.DB.PreferenceController
import com.twoam.cartello.Utilities.General.AppConstants
import com.twoam.cartello.Utilities.General.AppController
import com.twoam.cartello.Utilities.General.MyContextWrapper
import com.twoam.cartello.View.MainActivity
import com.twoam.cartello.View.CreateAddressActivity
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import java.util.*

/**
 * Created by Mokhtar on 6/28/2019.
 */

open class BaseDefaultActivity : AppCompatActivity(), OnItemClick {

    lateinit var mLoadingDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mLoadingDialog = Dialog(this)
        mLoadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mLoadingDialog.setContentView(R.layout.progress_bar)
        mLoadingDialog.window!!.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT)
        mLoadingDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        mLoadingDialog.setCancelable(false)


    }



    fun checkHasAddress(user: User) {

        var cities = ArrayList<City>()
        try {
            cities = PreferenceController.getInstance(AppController.getContext()).getCitiesPref(AppConstants.CITIES_DATA)!!
        } catch (ex: Exception) {
        }

        if (user.address != null && user.address!!.addresses.size > 0&& cities.count() > 0) {
            user.hasAddress = true
            PreferenceController.instance?.setUserPref(AppConstants.USER_DATA, user)
//            PreferenceController.getInstance(applicationContext).Set(AppConstants.HASADDRESS, AppConstants.TRUE)
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            startActivity(Intent(this, CreateAddressActivity::class.java))
            finish()
        }

    }

    fun saveUserData(user: User) {

        PreferenceController.getInstance(applicationContext).Set(AppConstants.IS_LOGIN, AppConstants.TRUE)
        PreferenceController.getInstance(applicationContext).setUserPref(AppConstants.USER_DATA, user)
        AppConstants.CurrentLoginUser = user
        checkHasAddress(user!!)
    }

    fun showAlertDialouge(message: String) {
        var alertDialouge = AlertDialog.Builder(this)
                .setMessage(message)
                .setCancelable(true)
                .setPositiveButton(getString(R.string.ok), { dialog, which -> })
        alertDialouge.create()
        alertDialouge.show()
    }

    fun showDialogue() {
        mLoadingDialog.show()
    }

    //hide progress bar Dialogue
    fun hideDialogue() {
        mLoadingDialog.dismiss()
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true

    }


    override fun onItemClicked(position: Int) {

    }


    override fun attachBaseContext(newBase: Context) {
        var lang: String
        try {
            if (PreferenceController.getInstance(this)[AppConstants.LANGUAGE] != AppConstants.ARABIC) {
                PreferenceController.getInstance(this).Set(AppConstants.LANGUAGE, AppConstants.ENGLISH)
                lang = AppConstants.ENGLISH
            } else {
                lang = AppConstants.ARABIC
            }
        } catch (e: Exception) {
            lang = AppConstants.ENGLISH
        }

        val langContext = MyContextWrapper.wrap(newBase, Locale(lang))
        super.attachBaseContext(CalligraphyContextWrapper.wrap(langContext))
    }


    protected fun hideKeyboard() {

        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        val view = this.currentFocus

        if (view != null) {

            imm.hideSoftInputFromWindow(view.windowToken, 0)

        }

    }


    companion object {

        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }
    }

}
