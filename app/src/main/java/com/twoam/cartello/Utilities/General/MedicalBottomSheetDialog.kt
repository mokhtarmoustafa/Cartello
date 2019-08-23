package com.twoam.cartello.Utilities.General


import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.support.design.widget.BottomSheetDialogFragment
import android.view.*
import android.widget.*
import com.twoam.cartello.R
import com.twoam.cartello.View.*
import pub.devrel.easypermissions.EasyPermissions
import android.R.attr.data
import android.support.v4.app.NotificationCompat.getExtras




class MedicalBottomSheetDialog : BottomSheetDialogFragment(), IBottomSheetCallback, View.OnClickListener {


    //region Members
    internal var view: ViewGroup? = null
    lateinit var layout: RelativeLayout
    private var listener: IBottomSheetCallback? = null
    private var action: Int = 0
    private lateinit var lLCapture: LinearLayout
    private lateinit var lLGallery: LinearLayout
    private lateinit var ivClose: ImageView
    val REQUEST_IMAGE_CAPTURE = 1
    val REQUEST_IMAGE_GALLERY = 2


    //endregion


    //region Properties
    var Action: Int //0 Camera 1 Gallery
        get() {
            return action
        }
        set(action) {
            this.action = action
        }

    //endregion

    //region Events
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        view = inflater.inflate(R.layout.bottom_sheet_medical, container, false) as ViewGroup
        layout = view!!.findViewById(R.id.rlOptions)

        init()
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is IBottomSheetCallback) {
            listener = context
        } else {
            throw ClassCastException(context.toString() + " must implement IBottomSheetCallback.onBottomSheetSelectedItem")
        }
    }

    override fun onBottomSheetSelectedItem(index: Int) {
        dialog.dismiss()
    }

    override fun onBottomSheetClosed(isClosed: Boolean) {
        dialog.dismiss()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ivClose -> {
                this.dismiss()
            }

            R.id.lLCapture -> {
                this.dismiss()
                openCamera()

            }
            R.id.lLGallery -> {
                this.dismiss()
//                navigate(1)
                val galleryPermissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)

                if (EasyPermissions.hasPermissions(context, galleryPermissions[0])) {
                    openGallery()
                } else {
                    EasyPermissions.requestPermissions(this, "Access for storage",
                            101, *galleryPermissions)
                }


            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val bitmap = data!!.extras.get("data") as Bitmap

            val extras = data.extras
            
            if (bitmap != null) {
                Toast.makeText(AppController.getContext(), "Image Saved!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(context, MedicalPrescriptionsDetailActivity::class.java).putExtra("image", bitmap))
            }

        }
        if (requestCode == REQUEST_IMAGE_GALLERY &&
                resultCode == Activity.RESULT_OK) {
            val contentURI = data!!.data ?: return
            val bitmap = MediaStore.Images.Media.getBitmap(activity!!.contentResolver, contentURI)
            startActivity(Intent(context, MedicalPrescriptionsDetailActivity::class.java).putExtra("image", bitmap))

        }
    }


    //endregion

    //region Helper Functions
    fun init() {

        lLCapture = layout.findViewById(R.id.lLCapture)
        lLGallery = layout.findViewById(R.id.lLGallery)

        ivClose = layout.findViewById(R.id.ivCloseMedical)

        ivClose.setOnClickListener(this)
        lLCapture.setOnClickListener(this)
        lLGallery.setOnClickListener(this)


    }

    private fun openCamera() {

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        this.startActivityForResult(intent,
                REQUEST_IMAGE_CAPTURE)

        AppConstants.CurrentCameraGAlleryAction = 0
    }


    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryIntent.type = "image/*"
        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        val mimeTypes = arrayOf("image/jpeg", "image/png")
        galleryIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        startActivityForResult(galleryIntent, REQUEST_IMAGE_GALLERY)
        AppConstants.CurrentCameraGAlleryAction = 1
    }

    //endregion

}