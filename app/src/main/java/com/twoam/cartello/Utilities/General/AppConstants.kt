package com.twoam.cartello.Utilities.General

import com.twoam.cartello.Model.User

/**
 * Created by Mokhtar on 6/18/2019.
 */
object AppConstants {

    const val SERVER_IP = "165.227.135.161"
    const val BASE_URL = "http://$SERVER_IP/trolley/public/api/"

    // region BASIC RESPONSE
    val STATUS_CODE = "status_code"
    val MESSAGE = "message"
    val DATA = "data"
    //endregion
    // region CODE RESPONSE
    val CODE_204 = 204
    val CODE_201 = 201
    val CODE_200 = 200
    val CODE_444 = 444
    //endregion
    //region languages
    val LANGUAGE = "lang"
    val ARABIC = "ar"
    val ENGLISH = "en"
    //endregion
    //region TOKEN
    var token = ""
    //endregion
    //region Auth
    const val SIGN_UP = "signup"
    const val URL_SIGN_UP = "customer/auth/signup"


    const val DOMAIN = "trolleyDomain"
    const val URL_LOGIN = "customer/auth"
    const val URL_LOG_OUT = "customer/auth/logout"
    const val URL_GUEST_LOGIN = "customer/auth/guest"
    const val URL_FORGET_PASSWORD = "customer/auth/forget_password"
    const val URL_VALODATE_REQUET_CODE = "customer/auth/validate_code"
    const val URL_RESET_PASSWORD = "customer/auth/reset_password"
    const val URL_SOCIAL_AUTH_FACEBOOK = "customer/auth/social/facebook"
    const val URL_SOCIAL_AUTH_GOOGLE = "customer/auth/social/google"

    const val URL_SOCIAL_SIGN_UP = "customer/auth/social_signup"
    const val URL_ADD_ADDRESS = "customer/profile/addresses"
    const val URL_UPDATE_ADDRESS = "customer/profile/addresses"
    const val URL_REMOVE_ADDRESS = "customer/profile/addresses/remove"
    const val URL_EDIT_PROFILE = "customer/profile/edit"
    const val URL_CHANGE_PASSWORD = "customer/profile/change_password"
    const val URL_ADD_TOKEN = "customer/profile/add_token"
    const val URL_GET_CITIES = "cities"
    const val URL_GET_CATEGORIES = "categories"
    const val URL_GET_DEPARTMENT = "customer/departments"
    const val URL_GET_DEPARTMENT_CATEGORIES = "customer/departments/categories"
    const val URL_GET_ADS = "customer/ads"
    const val URL_GET_SUB_CATEGORIES = "customer/categories/attributes/153"
    const val URL_GET_HOME_PRODUCTS = "customer/products/home"


    //endregion

    //region  app variables
    const val IS_LOGIN = "login"
    const val ADDRESS = "address"
    const val HASADDRESS = "hasAddress"
    const val TRUE = "true"
    const val FALSE = "false"
    const val USER_DATA = "userData"
    const val FACEBOOK = 1
    const val GOOGLE = 2
    const val CURRENTINDEXTAG="currentOption"
    var CURRENTSELECTEDINDEX = 0

    var CurrentLoginUser: User = User()

    const val BEARER = "Bearer "

    //facebook social login
    const val EMAIL = "email"
    const val PUBLIC_PROFILE = "public_profile"
    const val USER_PERMISSION = "user_friends"
    //endregion

    //region bottom sheet
     var isClosed = false
    //endregion
    //region others
    const val TOP_PROMOTIONS="Top Pormotions"
    const val MOST_SELLING="Most Selling"

    //endregion

}