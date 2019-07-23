package com.twoam.cartello.View

import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Toast
import com.twoam.Networking.INetworkCallBack
import com.twoam.Networking.NetworkManager
import com.twoam.cartello.Model.MedicalPrescriptions
import com.twoam.cartello.R
import com.twoam.cartello.Utilities.API.ApiResponse
import com.twoam.cartello.Utilities.API.ApiServices
import com.twoam.cartello.Utilities.Base.BaseDefaultActivity
import com.twoam.cartello.Utilities.General.AppConstants
import com.twoam.cartello.Utilities.General.AppController
import com.twoam.cartello.Utilities.General.MedicalBottomSheetDialog
import kotlinx.android.synthetic.main.activity_product_detail.*
import java.io.ByteArrayOutputStream

class ProductDetailActivity : BaseDefaultActivity(), View.OnClickListener {


    //region Members
    private lateinit var image: Bitmap
    private var bottom = MedicalBottomSheetDialog()
    //endregion

    //region Events

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        if (intent.hasExtra("image")) {
            image = intent.extras.get("image") as Bitmap
            ivImage.setImageBitmap(image)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tvRetakeImage -> {
                bottom.show(supportFragmentManager, "Custom Bottom Sheet")
            }
            R.id.btnSend -> {
                var encodeImage = encodeTobase64(image)
                var note = etNote.text.toString()
                if (encodeImage.isNotEmpty())
                    addMedical("my meds", note, encodeImage)
            }
        }
    }

    //endregion

    //region Helper Functions
    //endregion


    fun encodeTobase64(image: Bitmap): String {
        val baos = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b = baos.toByteArray()
        val imageEncoded = Base64.encodeToString(b, Base64.DEFAULT)

        Log.e("LOOK", imageEncoded)
        return imageEncoded
    }


    fun addMedical(name: String, note: String, image: String) {
        if (NetworkManager().isNetworkAvailable(AppController.getContext())) {
            var token = AppConstants.BEARER + AppConstants.CurrentLoginUser.token
            var request = NetworkManager().create(ApiServices::class.java)
            var endPoint = request.addMedical(token, name, note, image)
            NetworkManager().request(endPoint, object : INetworkCallBack<ApiResponse<MedicalPrescriptions>> {
                override fun onFailed(error: String) {
                    hideDialogue()
                    showAlertDialouge(error)
                }

                override fun onSuccess(response: ApiResponse<MedicalPrescriptions>) {
                    if (response.code == AppConstants.CODE_200) {
                        finish()
                        //navigate to medical fragment
                    } else {
                        Toast.makeText(AppController.getContext(), response.message, Toast.LENGTH_SHORT).show()
                    }

                }
            })
        } else {
            hideDialogue()
            showAlertDialouge(getString(R.string.error_no_internet))
        }
    }

    fun loadMedicalDetails() {}

}
