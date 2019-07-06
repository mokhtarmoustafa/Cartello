package com.twoam.cartello.View

import android.os.Bundle
import android.os.Handler
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.twoam.cartello.Model.SubCategory
import com.twoam.cartello.R
import com.twoam.cartello.Utilities.Adapters.AdsAdapter
import com.twoam.cartello.Utilities.Adapters.CategoryAdapter
import com.twoam.cartello.Utilities.Adapters.SubCategoryAdapter
import com.twoam.cartello.Utilities.General.AppController
import com.viewpagerindicator.CirclePageIndicator
import java.util.*

class HomeFragment : Fragment() {


    //region Members

    private var currentPage = 0
    private var NUM_PAGES = 0
    private val IMAGES = arrayOf(R.drawable.ic_cart1, R.drawable.ic_cart1, R.drawable.ic_cart1, R.drawable.ic_cart1, R.drawable.ic_cart1)
    private val ImagesArray = ArrayList<Int>()
    private var adapter: SubCategoryAdapter? = null
    private val myImageList = intArrayOf(R.drawable.ic_cart, R.drawable.ic_cart1, R.drawable.ic_cart, R.drawable.ic_cart1, R.drawable.ic_cart1)
    private val myImageNameList = arrayOf("Meat", "Milk", "Bread", "Cheese", "Chicken")
    private lateinit var recyclerSubCategory: RecyclerView
    private lateinit var tabs: TabLayout
    private lateinit var viewPager: ViewPager
    private lateinit var pager: ViewPager
    private lateinit var indicator: CirclePageIndicator
    //endregion

    //region Events

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(R.layout.fragment_home, container, false)

        recyclerSubCategory = view.findViewById(R.id.recyclerSubCategory)
        tabs = view.findViewById(R.id.tabs)
        viewPager = view.findViewById(R.id.viewPager)
        pager = view.findViewById(R.id.pager)
        indicator = view.findViewById(R.id.indicator)

        getAds()
        getCategories()
        getSubCategory()

        return view
    }
    //endregion

    //region Helper Functions

    private fun getCategories() {
        tabs.addTab(tabs.newTab().setText(getString(R.string.tab_home)))
        for (k in 0..9) {
            tabs.addTab(tabs.newTab().setText("" + k))
        }

        val adapter = CategoryAdapter(fragmentManager, tabs.tabCount)
        viewPager.adapter = adapter
        viewPager.offscreenPageLimit = 1
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        //Bonus Code : If your tab layout has more than 2 tabs then tab will scroll other wise they will take whole width of the screen
        if (tabs.tabCount === 2) {
            tabs.tabMode = TabLayout.MODE_FIXED
        } else {
            tabs.tabMode = TabLayout.MODE_SCROLLABLE
        }
    }

    private fun getSubCategory() {
        val list = ArrayList<SubCategory>()

        for (i in 0..4) {
            val fruitModel = SubCategory()
            fruitModel.name = myImageNameList[i]
            fruitModel.image = myImageList[i]
            list.add(fruitModel)
        }

        //get sub categories demo
        adapter = SubCategoryAdapter(AppController.getContext(), list)
        recyclerSubCategory?.adapter = adapter
        recyclerSubCategory.layoutManager = LinearLayoutManager(AppController.getContext(), LinearLayoutManager.HORIZONTAL, false)
    }

    private fun getAds() {
        for (i in 0 until IMAGES.size)
            ImagesArray.add(IMAGES[i])
        pager?.adapter = AdsAdapter(AppController.getContext(), ImagesArray)
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
