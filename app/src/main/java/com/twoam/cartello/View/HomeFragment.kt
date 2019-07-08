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
import com.twoam.cartello.Model.Product
import com.twoam.cartello.Model.SubCategory
import com.twoam.cartello.R
import com.twoam.cartello.Utilities.Adapters.AdsAdapter
import com.twoam.cartello.Utilities.Adapters.CategoryAdapter
import com.twoam.cartello.Utilities.Adapters.ProductAdapter
import com.twoam.cartello.Utilities.Adapters.SubCategoryAdapter
import com.twoam.cartello.Utilities.General.AppController
import com.viewpagerindicator.CirclePageIndicator
import java.util.*
import kotlin.collections.ArrayList

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
    private lateinit var recyclerTopPromotions: RecyclerView
    private lateinit var recyclerMostSelling: RecyclerView

    private lateinit var tabs: TabLayout
    private lateinit var viewPager: ViewPager
    private lateinit var pager: ViewPager
    private lateinit var indicator: CirclePageIndicator
    var NAME_ARG = "name"
    var AGE_ARG = "age"
    private var name: String? = null
    private var age: Int = 0
    //endregion

    //region Events

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val arguments = arguments

        if (arguments != null) {
            this.name = arguments.getString(NAME_ARG)
            this.age = arguments.getInt(AGE_ARG)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(R.layout.fragment_home, container, false)

        recyclerSubCategory = view.findViewById(R.id.recyclerSubCategory)
        tabs = view.findViewById(R.id.tabs)
        viewPager = view.findViewById(R.id.viewPager)
        pager = view.findViewById(R.id.pager)
        indicator = view.findViewById(R.id.indicator)
        recyclerTopPromotions = view.findViewById(R.id.recyclerTopPromotions)
        recyclerMostSelling = view.findViewById(R.id.recyclerMostSelling)

        getAds()
        getCategories()
        getSubCategory()
        getTopPromotionsProducts()
        getMostSellingProducts()


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

    fun newInstance(name: String, age: Int): HomeFragment {

        val args = Bundle()

        args.putString(NAME_ARG, name)
        args.putInt(AGE_ARG, age)

        val fragment = HomeFragment()

        fragment.arguments = args

        return fragment
    }

    fun getTopPromotionsProducts() {
        var list = ArrayList<Product>()
        list.add(Product("1", "Pampers baby dry junior 5 (11-25kg) 58", "R.drawable.ic_cart", "150", "135"))
        list.add(Product("1", "Product Name 2", "R.drawable.ic_cart", "190", "175"))
        list.add(Product("1", "Product Name 3", "R.drawable.ic_cart", "110", "105"))
        list.add(Product("1", "Product Name 4", "R.drawable.ic_cart", "168", "122"))
        list.add(Product("1", "Product Name 5", "R.drawable.ic_cart", "113", "100"))
        list.add(Product("1", "Product Name 1", "R.drawable.ic_cart", "150", "135"))
        list.add(Product("1", "Product Name 2", "R.drawable.ic_cart", "190", "175"))
        list.add(Product("1", "Product Name 3", "R.drawable.ic_cart", "110", "105"))
        list.add(Product("1", "Product Name 4", "R.drawable.ic_cart", "168", "122"))
        list.add(Product("1", "Product Name 5", "R.drawable.ic_cart", "113", "100"))
        var adapter = ProductAdapter(activity, list)
        recyclerTopPromotions.adapter = adapter
        recyclerTopPromotions.layoutManager = LinearLayoutManager(AppController.getContext(), LinearLayoutManager.HORIZONTAL, false)

    }

    fun getMostSellingProducts() {
        var list = ArrayList<Product>()
        list.add(Product("1", "Product Name 1", "R.drawable.ic_cart", "150", "135"))
        list.add(Product("1", "Product Name 2", "R.drawable.ic_cart", "190", "175"))
        list.add(Product("1", "Product Name 3", "R.drawable.ic_cart", "110", "105"))
        list.add(Product("1", "Product Name 4", "R.drawable.ic_cart", "168", "122"))
        list.add(Product("1", "Product Name 5", "R.drawable.ic_cart", "113", "100"))
        list.add(Product("1", "Product Name 1", "R.drawable.ic_cart", "150", "135"))
        list.add(Product("1", "Product Name 2", "R.drawable.ic_cart", "190", "175"))
        list.add(Product("1", "Product Name 3", "R.drawable.ic_cart", "110", "105"))
        list.add(Product("1", "Product Name 4", "R.drawable.ic_cart", "168", "122"))
        list.add(Product("1", "Product Name 5", "R.drawable.ic_cart", "113", "100"))
        var adapter = ProductAdapter(activity, list)
        recyclerMostSelling.adapter = adapter
        recyclerMostSelling.layoutManager = LinearLayoutManager(AppController.getContext(), LinearLayoutManager.HORIZONTAL, false)

    }
    //endregion
}
