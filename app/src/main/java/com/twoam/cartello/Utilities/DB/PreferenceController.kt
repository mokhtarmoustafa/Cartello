package com.twoam.cartello.Utilities.DB

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.text.TextUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.twoam.cartello.Model.*


class PreferenceController private constructor(context: Context, databaseName: String) {
    private val preferences: SharedPreferences = context.getSharedPreferences(databaseName, PRIVATE_MODE)

    //region General Helpers
    companion object {
        val DATABASE_NAME = "Cartello"
        var PRIVATE_MODE = 0
        var instance: PreferenceController? = null


        fun getInstance(context: Context): PreferenceController {
            if (instance == null) instance = PreferenceController(context, DATABASE_NAME)
            return instance!!
        }
    }
    // end region

    //region General Functions

    // SET THE KEY AN D VALUE
    fun Set(key: String, `val`: String) {
        preferences.edit().putString(key, `val`).apply()
    }


    // GET THE KEY
    operator fun get(key: String): String {
        return preferences.getString(key, "")
    }

    //CLEAR THE DATA
    fun clear(key: String) {
        Set(key, "")
    }

    //endregion


    //region USERS
// SET THE USER DATA
    fun setUserPref(key: String, values: User?) {
        val editor = preferences.edit()
        val gson = Gson()
        if (values == null) {
            return
        }
        val json = gson.toJson(values)

        editor.putString(key, json)
        editor.commit()
    }

    // GET THE USR DATA
    fun getUserPref(key: String): User? {
        val gson = Gson()
        val json = preferences.getString(key, null) ?: return null

        val type = object : TypeToken<User>() {
        }.type

        return gson.fromJson(json, type)
    }

    fun setCartPref(key: String, values: ArrayList<Product>?) {
        val editor = preferences.edit()
        val gson = Gson()
        if (values == null) {
            return
        }
        val json = gson.toJson(values)

        editor.putString(key, json)
        editor.commit()
    }

    // GET THE USR DATA
    fun getCartPref(key: String): ArrayList<Product>? {
        val gson = Gson()
        val json = preferences.getString(key, null) ?: return null

        val type = object : TypeToken<ArrayList<Product>>() {
        }.type

        return gson.fromJson(json, type)
    }



    fun setAddressPref(key: String, values: Address?) {
        val editor = preferences.edit()
        val gson = Gson()
        if (values == null) {
            return
        }
        val json = gson.toJson(values)

        editor.putString(key, json)
        editor.commit()
    }

    // GET THE Address Data
    fun getAddressPref(key: String): Address? {
        val gson = Gson()
        val json = preferences.getString(key, null) ?: return null

        val type = object : TypeToken<Address>() {
        }.type

        return gson.fromJson(json, type)
    }


    fun setCitiesPref(key: String, values: ArrayList<City>?) {
        val editor = preferences.edit()
        val gson = Gson()
        if (values == null) {
            return
        }
        val json = gson.toJson(values)

        editor.putString(key, json)
        editor.commit()
    }

    // GET THE Address Data
    fun getCitiesPref(key: String): ArrayList<City>? {
        val gson = Gson()
        val json = preferences.getString(key, null) ?: return null

        val type = object : TypeToken<ArrayList<City>>() {
        }.type

        return gson.fromJson(json, type)
    }


    fun setSearchPref(key: String, values: ArrayList<Search>?) {
        val editor = preferences.edit()
        val gson = Gson()
        if (values == null) {
            return
        }
        val json = gson.toJson(values)

        editor.putString(key, json)
        editor.commit()
    }

    // GET THE Address Data
    fun getSearchPref(key: String): ArrayList<Search>? {
        val gson = Gson()
        val json = preferences.getString(key, null) ?: return null

        val type = object : TypeToken<ArrayList<Search>>() {
        }.type

        return gson.fromJson(json, type)
    }


}

