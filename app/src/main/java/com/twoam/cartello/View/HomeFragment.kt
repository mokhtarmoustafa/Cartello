package com.twoam.cartello.View

import android.os.Bundle
import android.os.Handler
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.twoam.Networking.INetworkCallBack
import com.twoam.Networking.NetworkManager
import com.twoam.cartello.Model.*
import com.twoam.cartello.R
import com.twoam.cartello.Utilities.API.ApiResponse
import com.twoam.cartello.Utilities.API.ApiServices
import com.twoam.cartello.Utilities.Adapters.AdsAdapter
import com.twoam.cartello.Utilities.Adapters.CategoryAdapter
import com.twoam.cartello.Utilities.Adapters.ProductAdapter
import com.twoam.cartello.Utilities.Adapters.SubCategoryAdapter
import com.twoam.cartello.Utilities.Base.BaseFragment
import com.twoam.cartello.Utilities.General.AnimateScroll
import com.twoam.cartello.Utilities.General.AppConstants
import com.twoam.cartello.Utilities.General.AppController
import com.viewpagerindicator.CirclePageIndicator
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*
import kotlin.collections.ArrayList


class HomeFragment : BaseFragment() {



    //region Members

    private var currentPage = 0
    private var NUM_PAGES = 0
    private var adapter: SubCategoryAdapter? = null
    private lateinit var recyclerSubCategory: RecyclerView
    private lateinit var recyclerTopPromotions: RecyclerView
    private lateinit var recyclerMostSelling: RecyclerView
    private var categoriesList = ArrayList<Category>()
    private var adsList = ArrayList<Ads>()
    private var homeProductsList = ArrayList<HomeProducts>()
    private var topPromotionsList = ArrayList<Product>()
    private var mostSellingList = ArrayList<Product>()
    private lateinit var tabs: TabLayout
    private lateinit var viewPager: ViewPager
    private lateinit var pager: ViewPager
    private lateinit var indicator: CirclePageIndicator
    var NAME_ARG = "name"
    var AGE_ARG = "age"
    private var name: String? = null
    private var age: Int = 0
    //endregion

    //region Constructor
    fun newInstance(): HomeFragment {
        val fragment = HomeFragment()
        return fragment
    }

    //endregion

    //region Events


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(R.layout.fragment_home, container, false)
        init(view)
        if (NetworkManager().isNetworkAvailable(AppController.getContext())) {
            prepareCategoriesData(1)
            getAdsData()
            getHomeProducts()
        } else {
            showAlertDialouge(getString(R.string.error_no_internet))
        }

        return view
    }



    //endregion

    //region Helper Functions
    private fun init(view: View) {
        recyclerSubCategory = view.findViewById(R.id.recyclerSubCategory)
        tabs = view.findViewById(R.id.tabs)
        viewPager = view.findViewById(R.id.viewPager)
        pager = view.findViewById(R.id.pager)
        indicator = view.findViewById(R.id.indicator)
        recyclerTopPromotions = view.findViewById(R.id.recyclerTopPromotions)
        recyclerMostSelling = view.findViewById(R.id.recyclerMostSelling)
    }

    private fun getCategories(categoriesList: ArrayList<Category>) {
        tabs.addTab(tabs.newTab().setText(getString(R.string.tab_home)))

        for (category in categoriesList.indices) {
            var category = categoriesList[category]
            tabs.addTab(tabs.newTab().setText("" + category.name))
        }


        val adapter = CategoryAdapter(childFragmentManager, tabs.tabCount)
        viewPager.adapter = adapter
        viewPager.offscreenPageLimit = 1
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        //Bonus Code : If your tab layout has more than 2 tabs then tab will scroll other wise they will take whole width of the screen
        if (tabs.tabCount === 2) {
            tabs.tabMode = TabLayout.MODE_FIXED
        } else {
            tabs.tabMode = TabLayout.MODE_SCROLLABLE
        }

        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val position = tab.position - 1
                if (position == -1)
                    showSubCategoriesData(false)
                else {
                    var category = categoriesList[position]
                    getSubCategory(category.sub_categories)
                    showSubCategoriesData(true)
                }

            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    private fun prepareCategoriesData(lang: Int) {
        if (NetworkManager().isNetworkAvailable(AppController.getContext())) {
            var request = NetworkManager().create(ApiServices::class.java)
            var endPoint = request.getCategories(lang)
            NetworkManager().request(endPoint, object : INetworkCallBack<ApiResponse<ArrayList<Category>>> {
                override fun onFailed(error: String) {
                    hideDialogue()
//                    showAlertDialouge(error)
                }

                override fun onSuccess(response: ApiResponse<ArrayList<Category>>) {
                    if (response.code == AppConstants.CODE_200) {
                        categoriesList = response.data!!
                        getCategories(categoriesList)
                    } else {
                        hideDialogue()
                    }
                }
            })

        } else {
            hideDialogue()
            showAlertDialouge(getString(R.string.error_no_internet))
        }
    }

    private fun getSubCategory(subCategoriesList: ArrayList<SubCategory>) {

        //get sub categories demo
        adapter = SubCategoryAdapter(AppController.getContext(), subCategoriesList)
        recyclerSubCategory.adapter = adapter
        recyclerSubCategory.layoutManager = LinearLayoutManager(AppController.getContext(), LinearLayoutManager.HORIZONTAL, false)
    }

    fun showSubCategoriesData(show: Boolean) {
        if (show) {
            recyclerSubCategory.visibility = View.VISIBLE
            AnimateScroll.scrollToView(scrollViewHome, recyclerSubCategory)
        } else {
            recyclerSubCategory.visibility = View.GONE
        }
    }

    private fun prepareAdsData(adsList: ArrayList<Ads>) {

        pager.adapter = AdsAdapter(AppController.getContext(), adsList)
        indicator.setViewPager(pager)

        val density = resources.displayMetrics.density

        //Set circle indicator radius
        indicator.radius = 5 * density

        NUM_PAGES = adsList.size

        // Auto start of viewpager
        val handler = Handler()
        val Update = Runnable {
            if (currentPage === NUM_PAGES) {
                currentPage = 0
            }
            pager.setCurrentItem(currentPage++, true)
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

    private fun getAdsData(): ArrayList<Ads> {

        if (NetworkManager().isNetworkAvailable(AppController.getContext())) {
            var request = NetworkManager().create(ApiServices::class.java)
            var authorization = AppConstants.BEARER + AppConstants.CurrentLoginUser.token
            var endPoint = request.getAds(authorization)
            NetworkManager().request(endPoint, object : INetworkCallBack<ApiResponse<ArrayList<Ads>>> {
                override fun onFailed(error: String) {
                    hideDialogue()
//                    showAlertDialouge(error)
                }

                override fun onSuccess(response: ApiResponse<ArrayList<Ads>>) {
                    if (response.code == AppConstants.CODE_200) {
                        adsList = response.data!!
                        prepareAdsData(adsList)
                    } else {
                        Toast.makeText(AppController.getContext(), response.message, Toast.LENGTH_SHORT).show()
                    }
                }

            })
        } else {
            hideDialogue()
            showAlertDialouge(getString(R.string.error_no_internet))
        }

        return adsList
    }

    private fun getHomeProducts(): ArrayList<HomeProducts> {

        if (NetworkManager().isNetworkAvailable(AppController.getContext())) {
            var request = NetworkManager().create(ApiServices::class.java)
            var authorization = AppConstants.BEARER + AppConstants.CurrentLoginUser.token
            var endPoint = request.getHomeProducts(authorization)
            NetworkManager().request(endPoint, object : INetworkCallBack<ApiResponse<ArrayList<HomeProducts>>> {
                override fun onFailed(error: String) {
                    hideDialogue()
//                    showAlertDialouge(error)
                }

                override fun onSuccess(response: ApiResponse<ArrayList<HomeProducts>>) {
                    if (response.code == AppConstants.CODE_200) {
                        homeProductsList = response.data!!

                        rlTopPromotions?.visibility = View.VISIBLE
                        rlMostSelling?.visibility = View.VISIBLE

                        prepareHomeProductsData(homeProductsList)
                    } else {
                        Toast.makeText(AppController.getContext(), response.message, Toast.LENGTH_SHORT).show()
                    }
                }

            })
        } else {
            hideDialogue()
            showAlertDialouge(getString(R.string.error_no_internet))
        }

        return homeProductsList
    }

    private fun getTopPromotionsProducts(topPromotionsList: ArrayList<Product>) {


        var list = ArrayList<Product>()
        for (product in topPromotionsList.indices) {
            list.add(topPromotionsList[product])
        }
        var adapter = ProductAdapter(activity, topPromotionsList)
        recyclerTopPromotions.adapter = adapter
        recyclerTopPromotions.layoutManager = LinearLayoutManager(AppController.getContext(), LinearLayoutManager.HORIZONTAL, false)

    }

    private fun getMostSellingProducts(mostSellingList: ArrayList<Product>) {


        var list = ArrayList<Product>()
        for (product in mostSellingList.indices) {
            list.add(mostSellingList[product])
        }
        var adapter = ProductAdapter(activity, list)
        recyclerMostSelling.adapter = adapter
        recyclerMostSelling.layoutManager = LinearLayoutManager(AppController.getContext(), LinearLayoutManager.HORIZONTAL, false)

    }

    private fun prepareHomeProductsData(homeProductList: ArrayList<HomeProducts>) {
        for (productIndex in homeProductList.indices) {
            var product = homeProductList[productIndex]
            if (product.name == AppConstants.TOP_PROMOTIONS) {
                topPromotionsList = product.products!!
                getTopPromotionsProducts(topPromotionsList)
            } else
                mostSellingList = product.products!!
            getMostSellingProducts(mostSellingList)
        }
    }
    //endregion
}
