package com.twoam.cartello.View

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.twoam.Networking.INetworkCallBack
import com.twoam.Networking.NetworkManager
import com.twoam.cartello.Model.MedicalPrescriptions
import com.twoam.cartello.R
import com.twoam.cartello.Utilities.API.ApiResponse
import com.twoam.cartello.Utilities.API.ApiServices
import com.twoam.cartello.Utilities.Base.BaseDefaultActivity
import com.twoam.cartello.Utilities.General.AppConstants
import com.twoam.cartello.Utilities.General.AppController
import com.twoam.cartello.Utilities.Interfaces.IBottomSheetCallback
import com.twoam.cartello.Utilities.General.MedicalBottomSheetDialog
import kotlinx.android.synthetic.main.activity_medical_prescriptions_detail.*
import java.io.ByteArrayOutputStream
import android.graphics.drawable.BitmapDrawable


class MedicalPrescriptionsDetailActivity : BaseDefaultActivity(), IBottomSheetCallback, View.OnClickListener {


    //region Members
    private var image: Bitmap? = null
    private var bottom = MedicalBottomSheetDialog()
    var listener: IBottomSheetCallback? = null
    val REQUEST_IMAGE_CAPTURE = 1
    val REQUEST_IMAGE_GALLERY = 2
    val REQUEST_CLOSE_ACTIVITY = 3
    //endregion

    //region Events

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medical_prescriptions_detail)

        tvRetakeImage.setOnClickListener(this)
        btnSend.setOnClickListener(this)

        if (intent.hasExtra("image")) {
            if (AppConstants.CurrentCameraGAlleryAction == 1) {
                var data = intent.extras.get("image")
                Glide.with(this)
                        .load(data)
                        .into(ivImage)

            } else {
                image = intent.extras.get("image") as Bitmap
                ivImage.setImageBitmap(image)
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val bitmap = data!!.extras.get("data") as Bitmap

            if (bitmap != null) {
                Toast.makeText(AppController.getContext(), "Image Saved!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@MedicalPrescriptionsDetailActivity, MedicalPrescriptionsDetailActivity::class.java).putExtra("image", bitmap))
            }

        }


        if (requestCode == REQUEST_IMAGE_GALLERY &&
                resultCode == Activity.RESULT_OK) {
            // mImageBitmap reduce image quality -50% and save it in directory
            //mImageBitmap convert to thumbnail and write it to file and update grid
            val contentURI = data!!.data ?: return
            Toast.makeText(AppController.getContext(), "Image Saved!", Toast.LENGTH_SHORT).show()

        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tvRetakeImage -> {

                bottom.show(supportFragmentManager, "Custom Bottom Sheet")
            }
            R.id.btnSend -> {
                //get bitmap from image view
                if (image == null)
                    image = (ivImage.drawable as BitmapDrawable).bitmap

                var encodeImage = encodeTobase64(image!!)
                var note = etNote.text.toString()

                if (encodeImage.isNotEmpty())
                    showDialogue()

                addMedical("my meds", note, encodeImage)
            }
        }
    }

    override fun onBottomSheetClosed(isClosed: Boolean) {

    }

    override fun onBottomSheetSelectedItem(index: Int) {

    }

    override fun onBackPressed() {
        super.onBackPressed()
        listener?.onBottomSheetSelectedItem(REQUEST_CLOSE_ACTIVITY)
        finish()

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
                        hideDialogue()
                        listener?.onBottomSheetClosed(true)
                        finish()
                        //navigate to medical fragment
                    } else {
                        hideDialogue()
                        Toast.makeText(AppController.getContext(), response.message, Toast.LENGTH_SHORT).show()
                    }

                }
            })
        } else {
            hideDialogue()
            showAlertDialouge(getString(R.string.error_no_internet))
        }
    }


}
