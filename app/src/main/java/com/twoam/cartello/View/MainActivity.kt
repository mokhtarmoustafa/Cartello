package com.twoam.cartello.View

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.twoam.cartello.R
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import com.twoam.cartello.Model.SubCategory
import com.twoam.cartello.Utilities.Adapters.AdsAdapter
import com.twoam.cartello.Utilities.Adapters.CategoryAdapter
import com.twoam.cartello.Utilities.Adapters.SubCategoryAdapter
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {


    //region Members
    private var currentPage = 0
    private var NUM_PAGES = 0
    private val IMAGES = arrayOf(R.drawable.ic_cart1, R.drawable.ic_cart1, R.drawable.ic_cart1, R.drawable.ic_cart1, R.drawable.ic_cart1)
    private val ImagesArray = ArrayList<Int>()

    private var imageModelArrayList: ArrayList<SubCategory>? = null
    private var adapter: SubCategoryAdapter? = null
    private val myImageList = intArrayOf(R.drawable.ic_cart, R.drawable.ic_cart1, R.drawable.ic_cart, R.drawable.ic_cart1, R.drawable.ic_cart1)
    private val myImageNameList = arrayOf("Meat", "Milk", "Bread", "Cheese", "Chicken")
    //endregion

    //region Events
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        getAds()

        getCategories()

        //get sub categories demo
        imageModelArrayList = getSubCategory()
        adapter = SubCategoryAdapter(this, imageModelArrayList)
        recyclerSubCategory?.adapter = adapter
        recyclerSubCategory?.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
    }

    //endregion


    //region Helper Functions
    private fun getCategories() {
        tabs.addTab(tabs.newTab().setText(getString(R.string.tab_home)))
        for (k in 0..9) {
            tabs.addTab(tabs.newTab().setText("" + k))
        }

        val adapter = CategoryAdapter(supportFragmentManager, tabs.getTabCount())
        viewPager.setAdapter(adapter)
        viewPager.setOffscreenPageLimit(1)
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        //Bonus Code : If your tab layout has more than 2 tabs then tab will scroll other wise they will take whole width of the screen
        if (tabs.getTabCount() === 2) {
            tabs.setTabMode(TabLayout.MODE_FIXED)
        } else {
            tabs.setTabMode(TabLayout.MODE_SCROLLABLE)
        }
    }

    private fun getSubCategory(): ArrayList<SubCategory> {
        val list = ArrayList<SubCategory>()

        for (i in 0..4) {
            val fruitModel = SubCategory()
            fruitModel.name = myImageNameList[i]
            fruitModel.image = myImageList[i]
            list.add(fruitModel)
        }

        return list
    }

    private fun getAds() {
        for (i in 0 until IMAGES.size)
            ImagesArray.add(IMAGES[i])
        pager?.adapter = AdsAdapter(this@MainActivity, ImagesArray)
        indicator.setViewPager(pager)

        val density = resources.displayMetrics.density

        //Set circle indicator radius
        indicator.radius = 5 * density

        NUM_PAGES = IMAGES.size

        // Auto start of viewpager
        val handler = Handler()
        val Update = Runnable {
            if (currentPage === NUM_PAGES) {
                currentPage = 0
            }
            pager?.setCurrentItem(currentPage++, true)
        }
        val swipeTimer = Timer()
        swipeTimer.schedule(object : TimerTask() {
            override fun run() {
                handler.post(Update)
            }
        }, 1500, 3000)

        // Pager listener over indicator
        indicator.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageSelected(position: Int) {
                currentPage = position

            }

            override fun onPageScrolled(pos: Int, arg1: Float, arg2: Int) {

            }

            override fun onPageScrollStateChanged(pos: Int) {

            }
        })

    }
    //endregion


}
