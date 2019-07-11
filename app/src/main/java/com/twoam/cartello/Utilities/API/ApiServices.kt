package com.twoam.cartello.Utilities.API

import com.twoam.cartello.Model.Address
import com.twoam.cartello.Model.Category
import com.twoam.cartello.Model.City
import com.twoam.cartello.Model.User
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
    ): Call<ApiResponse<User>>

    @POST(AppConstants.URL_LOGIN)
    fun logIn(@Query("email") email: String, @Query("password") password: String)
            : Call<ApiResponse<User>>

    @POST(AppConstants.URL_SOCIAL_AUTH_FACEBOOK)
    fun logInSocialFacebook(@Query("access_token") access_token: String)
            : Call<ApiResponse<User>>

    @POST(AppConstants.URL_SOCIAL_AUTH_GOOGLE)
    fun logInSocialGoogle(@Query("access_token") access_token: String)
            : Call<ApiResponse<User>>

    @POST(AppConstants.URL_LOG_OUT)
    fun logOut(@Query("email") email: String, @Query("password") password: String)
            : Call<ApiResponse<User>>

    @POST(AppConstants.URL_GUEST_LOGIN)
    fun guestLogin(): Call<ApiResponse<User>>

    @POST(AppConstants.URL_FORGET_PASSWORD)
    fun forgetPassword(@Query("email") email: String): Call<ApiResponse<Boolean>>

    @POST(AppConstants.URL_VALODATE_REQUET_CODE)
    fun validateRequestCode(@Query("code") code: String, @Query("email") email: String)
            : Call<ApiResponse<Boolean>>

    @POST(AppConstants.URL_RESET_PASSWORD)
    fun resetPassword(@Query("email") email: String, @Query("code") code: String, @Query("password") password: String)
            : Call<ApiResponse<User>>


    @POST(AppConstants.URL_SOCIAL_SIGN_UP)
    fun socialSignUp(@Query("name") name: String, @Query("phone") phone: String, @Query("birthdate") birthdate: String)
            : Call<ApiResponse<User>>


    @GET(AppConstants.URL_GET_CITIES)
    fun getCities()
            : Call<ApiResponse<ArrayList<City>>>

    @POST(AppConstants.URL_ADD_ADDRESS)
    fun addAddress(@Header("Authorization") token: String, @Query("name") name: String, @Query("city_id") city_id: String,
                   @Query("area_id") area_id: String, @Query("address") address: String,
                   @Query("apartment") apartment: String, @Query("floor") floor: String, @Query("landmark") landmark: String
    ): Call<ApiResponse<Address>>

    @GET(AppConstants.URL_GET_CATEGORIES)
    fun getCategories(@Header("lang") lang: Int): Call<ApiResponse<ArrayList<Category>>>

}