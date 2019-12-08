package com.twoam.cartello.Utilities.API

import com.google.gson.JsonObject
import com.twoam.cartello.Model.*
import com.twoam.cartello.Utilities.General.AppConstants
import retrofit2.Call
import retrofit2.http.*
import com.twoam.cartello.Model.User
import org.json.JSONObject
import retrofit2.http.POST
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded


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

    @POST(AppConstants.URL_CHANGE_PASSWORD)
    fun changePassword(@Header("Authorization") token: String, @Query("old_password") oldPassword: String, @Query("password") newPassword: String)
            : Call<ApiResponse<Boolean>>


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
    ): Call<ApiResponse<User>>


    @POST(AppConstants.URL_UPDATE_ADDRESS + "/{addressId}")
    fun updateAddress(@Header("Authorization") token: String, @Path("addressId") userId: Int, @Query("name") name: String, @Query("city_id") city_id: String,
                      @Query("area_id") area_id: String, @Query("address") address: String,
                      @Query("apartment") apartment: String, @Query("floor") floor: String, @Query("landmark") landmark: String
    ): Call<ApiResponse<Addresses>>

    @POST(AppConstants.URL_REMOVE_ADDRESS + "/{addressId}")
    fun removeAddress(@Path("addressId") userId: Int, @Header("Authorization") token: String): Call<ApiResponse<Addresses>>

    @GET(AppConstants.URL_GET_CATEGORIES)
    fun getCategories(@Header("lang") lang: Int): Call<ApiResponse<ArrayList<Category>>>

    @GET(AppConstants.URL_GET_SUB_CATEGORIES + "/{productId}")
    fun getSubCategories(@Header("Authorization") token: String,
                         @Path("productId") productId: Int): Call<ApiResponse<ArrayList<Product>>>

    @GET(AppConstants.URL_GET_ADS)
    fun getAds(@Header("Authorization") token: String): Call<ApiResponse<ArrayList<Ads>>>

    @GET(AppConstants.URL_GET_HOME_PRODUCTS)
    fun getHomeProducts(@Header("Authorization") token: String): Call<ApiResponse<ArrayList<HomeProducts>>>

    @POST(AppConstants.URL_EDIT_PROFILE)
    fun editProfile(@Header("Authorization") token: String,
                    @Query("phone") phone: String,
                    @Query("name") name: String,
                    @Query("birthdate") birthdate: String

    ): Call<ApiResponse<User>>

    @GET(AppConstants.URL_GET_MEDICAL_PRESCRIPTIONS_GET_ALL)
    fun getAllMedicalPrescriptions(@Header("Authorization") token: String): Call<ApiResponse<ArrayList<MedicalPrescriptions>>>


    @POST(AppConstants.URL_GET_MEDICAL_ADD)
    @FormUrlEncoded //for upload image
    fun addMedical(@Header("Authorization") token: String,
                   @Query("name") name: String,
                   @Query("note") note: String,
                   @Field("image") image: String): Call<ApiResponse<MedicalPrescriptions>>


    @GET(AppConstants.URL_GET_ORDERS)
    fun getOrders(@Header("Authorization") token: String): Call<ApiResponse<ArrayList<Order>>>

    @POST(AppConstants.URL_CANCEL_ORDERS + "/{orderID}")
    fun cancelOrder(@Header("Authorization") token: String,
                    @Path("orderID") orderId: Int): Call<ApiResponse<Order>>

    @GET(AppConstants.URL_GET_PRODUCT_DETAILS + "/{productId}")
    fun getProductDetails(@Header("Authorization") token: String,
                          @Path("productId") productId: Int): Call<ApiResponse<ProductDetails>>

    @POST(AppConstants.URL_CANCEL_ORDERS)
    fun checkPromo(@Header("Authorization") token: String,
                   @Query("promo") promo: String,
                   @Query("amount") amount: Double)
            : Call<ApiResponse<Boolean>>

    //    @FormUrlEncoded
//    @POST(AppConstants.URL_ORDERS_CREATE)
//    fun createOrder(@Header("Authorization") token: String,
//                    @Field("payment_method") payment_method: Int,
//                    @Field("items") items: Array<Items?>,
//                    @Field("address_id") address_id: Int
//    ): Call<ApiResponse<Order>>

    @FormUrlEncoded
    @PATCH(AppConstants.URL_ORDERS_CREATE)
    fun createOrder(@Header("Authorization") token: String,
                    @Field("payment_method") payment_method: Int,
                    @Field("items") items: Array<Items?>,
//                    @FieldMap items: Map<String, Array<Items?>>,
                    @Field("address_id") address_id: Int


    ): Call<ApiResponse<Order>>

    @Headers("Content-Type: application/json")
    @POST(AppConstants.URL_ORDERS_CREATE)
    fun createOrder1(@Header("Authorization") token: String,@Body jsonObject: JSONObject
    ): Call<ApiResponse<Order>>

    @GET(AppConstants.URL_PRODUCT_FAVOURITES)
    fun getFavourites(@Header("Authorization") token: String): Call<ApiResponse<ArrayList<Product>>>

    @POST(AppConstants.URL_PRODUCT_ADD_TO_FAVOURITE + "/{productId}")
    fun addToFavourite(@Header("Authorization") token: String,
                       @Path("productId") productId: Int
    )
            : Call<ApiResponse<Boolean>>

    @POST(AppConstants.URL_PRODUCT_REMOVE_FROM_FAVOURITE + "/{productId}")
    fun removeFromFavourite(@Header("Authorization") token: String,
                            @Path("productId") productId: Int
    )
            : Call<ApiResponse<Boolean>>

    @GET(AppConstants.URL_PRODUCTS_SEARCH)
    fun searchProducts(@Header("Authorization") token: String,
                       @Query("q") searchValue: String)
            : Call<ApiResponse<ArrayList<Product>>>

    @POST(AppConstants.URL_PRODUCTS_FILTER + "/{filterValue}")
    fun filterProducts(@Header("Authorization") token: String,
                       @Path("filterValue") filterValue: String)
            : Call<ApiResponse<ArrayList<Product>>>


}

