package com.twoam.cartello.View


import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.view.View
import com.twoam.cartello.Model.Order
import com.twoam.cartello.R
import com.twoam.cartello.Utilities.Base.BaseDefaultActivity
import com.twoam.cartello.Utilities.Base.BaseFragment
import com.twoam.cartello.Utilities.General.AppConstants
import com.twoam.cartello.Utilities.General.CustomBottomSheetDialog
import com.twoam.cartello.Utilities.General.IBottomSheetCallback
import com.twoam.cartello.Utilities.General.IOrderCallback
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseDefaultActivity(), View.OnClickListener, IBottomSheetCallback,IOrderCallback {



    //region Members
    private var homeBottom: CustomBottomSheetDialog = CustomBottomSheetDialog()
    private var isOpened = false
    val homeFragment = HomeFragment()
    val medicalFragment = MedicalPrescriptionsFragment()
    val orderFragment = OrdersFragment()
    val moreFragment = MoreFragment()
    val fm = supportFragmentManager
    var active = BaseFragment()
    var bitmap: Bitmap? = null
    //endregion

    //region Events
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()

        fm.beginTransaction().replace(R.id.layout_container, homeFragment, "homeFragment").commit()
        fm.beginTransaction().add(R.id.layout_container, medicalFragment, "medicalFragment").hide(medicalFragment).commit()
        fm.beginTransaction().add(R.id.layout_container, orderFragment, "orderFragment").hide(orderFragment).commit()
        fm.beginTransaction().add(R.id.layout_container, moreFragment, "moreFragment").hide(moreFragment).commit()

        active = homeFragment
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.rlHome -> {
                homeBottom.show(fm, "Custom Bottom Sheet")
                isOpened = true

            }
            R.id.ivCart, R.id.cart_badge -> {
                var intent = Intent(this@MainActivity, CartActivity::class.java)
                startActivity(intent)
            }

            R.id.ivSearch -> {

            }
        }
    }

    override fun onOrderCancelled(isCanceled: Boolean, order: Order?) {
        if (isCanceled)
        {
           this.orderFragment.cancelOrder(order!!)
        }
    }

    override fun onBackPressed() {
        if (fm.backStackEntryCount > 0) {
            fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            active = homeFragment
            tvMainHome.text = getString(R.string.tab_home)
            AppConstants.CURRENTSELECTEDINDEX = 0
        } else {
            super.onBackPressed()
        }
    }


    override fun onBottomSheetClosed(isClosed: Boolean) {

    }

    override fun onBottomSheetSelectedItem(index: Int) {
        when (index) {
            0 -> {
                if (active == homeFragment)
                    return
                fm.beginTransaction().hide(active).show(homeFragment).commit()
                active = homeFragment
            }
            1 -> {
                if (active == medicalFragment)
                    return
                fm.beginTransaction().hide(active).show(medicalFragment).addToBackStack(null).commit()
                active = medicalFragment
            }
            2 -> {
                if (active == orderFragment)
                    return
                fm.beginTransaction().hide(active).show(orderFragment).addToBackStack(null).commit()
                active = orderFragment
            }
            3 -> {
                if (active == moreFragment)
                    return
                fm.beginTransaction().hide(active).show(moreFragment).addToBackStack(null).commit()
                active = moreFragment
            }


        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {

            if (AppConstants.CurrentCameraGAlleryAction == 0) //camera
            {
                bitmap = data!!.extras.get("data") as Bitmap
                var intent = Intent(this@MainActivity, ProductDetailActivity::class.java)
                startActivity(intent.putExtra("image", bitmap))
            } else if (AppConstants.CurrentCameraGAlleryAction == 1) //gallery
            {

                val selectedImage = data!!.data
                if (selectedImage != null) {
                    var intent = Intent(this@MainActivity, ProductDetailActivity::class.java)
                    startActivity(intent.putExtra("image", selectedImage))
                }
            }


        }
    }

    //endregion


    //region Helper Functions

    private fun init() {
        rlHome.setOnClickListener(this)
        cart_badge.setOnClickListener(this)
        ivCart.setOnClickListener(this)
        ivSearch.setOnClickListener(this)
    }


    //endregion


}
