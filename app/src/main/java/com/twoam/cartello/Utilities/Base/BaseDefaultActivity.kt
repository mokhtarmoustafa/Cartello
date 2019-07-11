package com.twoam.cartello.Utilities.Base

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.view.MenuItemCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.AppCompatDelegate
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.RelativeLayout
import android.widget.Toast
import com.twoam.cartello.Model.User
import com.twoam.cartello.R
import com.twoam.cartello.Utilities.DB.PreferenceController
import com.twoam.cartello.Utilities.General.AppConstants
import com.twoam.cartello.Utilities.General.MyContextWrapper
import com.twoam.cartello.View.MainActivity
import com.twoam.cartello.View.NewAddressActivity
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

        if (user.addresses != null && user.addresses!!.size > 0) {
            startActivity(Intent(this, MainActivity::class.java))
            PreferenceController.getInstance(applicationContext).Set(AppConstants.HASADDRESS, AppConstants.TRUE)
        } else {
            startActivity(Intent(this, NewAddressActivity::class.java))
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
