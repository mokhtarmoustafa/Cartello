package com.twoam.cartello.View

import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.twoam.cartello.R
import com.twoam.cartello.R.id.ivHome
import com.twoam.cartello.R.id.tvMainHome
import com.twoam.cartello.Utilities.Base.BaseDefaultActivity
import com.twoam.cartello.Utilities.General.CustomBottomSheetDialog
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseDefaultActivity(), View.OnClickListener {


    //region Members
    private var homeBottom: CustomBottomSheetDialog = CustomBottomSheetDialog()
    private var isOpened = false
    //endregion

    //region Events
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()

//        if (savedInstanceState == null) {
        supportFragmentManager.beginTransaction().replace(R.id.layout_container, HomeFragment()).commit()
//            ivHome.setImageResource(R.drawable.ic_home_select)
//            tvMainHome.setTextColor(Color.parseColor( "#38a0cd"))
//        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ivHome, R.id.tvMainHome -> {
                homeBottom.show(supportFragmentManager, "Custom Bottom Sheet")
                isOpened = true

            }
        }
    }

    override fun onBackPressed() {
        if (fragmentManager.backStackEntryCount > 0) {
            fragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }

    //endregion


    //region Helper Functions

    private fun init() {
        tvMainHome.setOnClickListener(this)
        ivHome.setOnClickListener(this)
    }


    //endregion


}
