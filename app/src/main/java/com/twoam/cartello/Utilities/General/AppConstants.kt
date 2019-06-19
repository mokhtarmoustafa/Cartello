package com.twoam.cartello.Utilities.General

/**
 * Created by Mokhtar on 6/18/2019.
 */
object AppConstants {

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
    //region TOKEN
    var token = ""
    //endregion
    //region Auth
    const val SERVER_IP = "52.174.22.188"

    const val SIGN_UP = "signup"
    const val URL_SIGN_UP = "$SERVER_IP/electa/public/api/customer/auth/signup"

    const val LOGIN = "auth"
    const val DOMAIN = "trolleyDomain"
    const val URL_LOGIN = "$SERVER_IP/electa/public/api/customer/auth"
    const val URL_LOG_OUT = "{{$DOMAIN}}/api/customer/auth/logout"
    const val URL_GUEST_LOGIN = "$SERVER_IP/electa/public/api/customer/auth/guest"
    const val URL_FORGET_PASSWORD = "$SERVER_IP/electa/public/api/customer/auth/forget_password"
    const val URL_VALODATE_REQUET_CODE = "$SERVER_IP/electa/electa/public/api/customer/auth/validate_code"
    const val URL_RESET_PASSWORD = "$SERVER_IP/electa/public/api/customer/auth/reset_password"
    const val URL_SOCIAL_AUTH = "$SERVER_IP/electa/public/api/customer/auth/social"
    const val URL_SOCIAL_SIGN_UP = "{{$DOMAIN}}/api/customer/auth/social_signup"
    const val URL_ADD_ADDRESS = "{{$DOMAIN}}/api/customer/profile/addresses"
    const val URL_UPDATE_ADDRESS = "{{$DOMAIN}}/api/customer/profile/addresses"
    const val URL_REMOVE_ADDRESS = "{{$DOMAIN}}/api/customer/profile/addresses/remove"
    const val URL_EDIT_PROFILE = "{{$DOMAIN}}/api/customer/profile/edit"
    const val URL_CHANGE_PASSWORD = "{{$DOMAIN}}/api/customer/profile/change_password"
    const val URL_ADD_TOKEN = "{{$DOMAIN}}/api/customer/profile/add_token"
    const val URL_GET_CITIES = "{{$DOMAIN}}/api/cities"
    const val URL_GET_CATEGORIES = "$SERVER_IP/electa/public/api/categories"
    const val URL_GET_DEPARTMENT="$SERVER_IP/electa/public/api/customer/departments"
    const val URL_GET_DEPARTMENT_CATEGORIES = "$SERVER_IP/electa/public/api/customer/departments/categories"
    const val URL_GET_ADS = "{{$DOMAIN}}/api/customer/ads"
    const val URL_GET_SUB_CATEGORIES = "$SERVER_IP/electa/public/api/customer/categories/attributes/153"



    //endregion


}