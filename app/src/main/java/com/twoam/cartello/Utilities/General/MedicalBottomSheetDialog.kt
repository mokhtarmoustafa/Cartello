package com.twoam.cartello.Utilities.General


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
import com.twoam.cartello.R.id.*
import com.twoam.cartello.Utilities.DB.PreferenceController
import com.twoam.cartello.View.*
import kotlinx.android.synthetic.main.bottom_sheet_close.*


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
                openGallery()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val bitmap = data!!.extras.get("data") as Bitmap


            if (bitmap != null) {
                Toast.makeText(AppController.getContext(), "Image Saved!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(context, ProductDetailActivity::class.java).putExtra("image", bitmap))
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

    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        activity!!.startActivityForResult(galleryIntent, REQUEST_IMAGE_GALLERY)
    }

    private fun openCamera() {
//        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
//            takePictureIntent.resolveActivity(AppController.getContext().packageManager)?.also {
//                activity!!.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
//            }
//        }

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        this.startActivityForResult(intent,
                REQUEST_IMAGE_CAPTURE)
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

    fun navigate(index: Int) {
        listener?.onBottomSheetSelectedItem(index)
    }

    private fun logOut() {
        PreferenceController.getInstance(AppController.getContext()).clear(AppConstants.IS_LOGIN)
        PreferenceController.getInstance(AppController.getContext()).clear(AppConstants.USER_DATA)
        context?.startActivity(Intent(context, LoginActivity::class.java))
        activity?.onBackPressed()
    }
    //endregion

}