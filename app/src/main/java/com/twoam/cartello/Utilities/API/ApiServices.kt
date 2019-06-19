package com.twoam.cartello.Utilities.API

import com.twoam.cartello.Model.Customer
import com.twoam.cartello.Utilities.General.AppConstants
import retrofit2.Call
import retrofit2.http.*


/**
 * Created by Mokhtar on 6/18/2019.
 */
interface ApiServices {

    @POST(AppConstants.URL_SIGN_UP)
    fun signUp(@Query("name") name: String, @Query("email") email: String,
               @Query("phone") phone: String, @Query("birthdate") birthdate: String,
               @Query("password") password: String, @Query("password_confirm") password_confirm: String
    ): Call<ApiResponse<Customer>>

    @POST(AppConstants.URL_LOGIN)
    fun logIn(@Query("email") email: String, @Query("password") password: String)
            : Call<ApiResponse<Customer>>

    @POST(AppConstants.URL_LOG_OUT)
    fun logOut(@Query("email") email: String, @Query("password") password: String)
            : Call<ApiResponse<Customer>>

    @POST(AppConstants.URL_GUEST_LOGIN)
    fun guestLogin(): Call<ApiResponse<Customer>>

    @POST(AppConstants.URL_FORGET_PASSWORD)
    fun forgetPassword(@Query("email") email: String): Call<ApiResponse<Boolean>>

    @POST(AppConstants.URL_VALODATE_REQUET_CODE)
    fun validateRequestCode(@Query("code") code: String, @Query("email") email: String)
            : Call<ApiResponse<Boolean>>

    @POST(AppConstants.URL_RESET_PASSWORD)
    fun resetPassword(@Query("email") email: String, @Query("code") code: String, @Query("password") password: String)
            : Call<ApiResponse<Customer>>

    @POST(AppConstants.URL_SOCIAL_AUTH)
    fun socialAuth(@Query("facebook") facebook: String)
            : Call<ApiResponse<Customer>>


    @POST(AppConstants.URL_SOCIAL_SIGN_UP)
    fun socialSignUp(@Query("name") name: String, @Query("phone") phone: String, @Query("birthdate") birthdate: String)
            : Call<ApiResponse<Customer>>

}