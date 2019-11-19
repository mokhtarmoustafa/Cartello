package com.twoam.cartello.Utilities.widget

import android.app.ProgressDialog
import android.content.Context
import android.util.Log

import com.twoam.cartello.R


class CustomProgressDialog(internal var context: Context) {
    private var progressDialog: ProgressDialog? = null

    val instance: ProgressDialog
        get() {
            if (progressDialog == null) {
                Log.d("progress", "progress")
                progressDialog = ProgressDialog(context, R.style.AppCompatAlertDialogStyle)
                progressDialog!!.setMessage(context.getString(R.string.loading))
                progressDialog!!.setCancelable(false)
                return progressDialog!!
            } else {
                Log.d("progress", "null" + progressDialog!!.isShowing)

                return progressDialog!!
            }
        }

    fun hide() {
        if (progressDialog != null)
            progressDialog!!.hide()
    }
}
