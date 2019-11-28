package com.twoam.cartello.View

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import com.twoam.cartello.Model.Search
import com.twoam.cartello.R
import com.twoam.cartello.Utilities.Adapters.SearchAdapter
import com.twoam.cartello.Utilities.Base.BaseDefaultActivity
import com.twoam.cartello.Utilities.DB.PreferenceController
import com.twoam.cartello.Utilities.General.AppConstants
import com.twoam.cartello.Utilities.General.AppController
import kotlinx.android.synthetic.main.activity_search.*
import java.lang.Exception

class SearchActivity : BaseDefaultActivity(), View.OnClickListener {

    //region Members
    var searchList = ArrayList<Search>()
    //endregion

    //region Events
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        init()
        getSearchData()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ivClose, R.id.cvClose -> {
                finish()
            }
            R.id.tvClearHistory -> {
                clearHistory()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        finish()
    }

    override fun onResume() {
        super.onResume()
        getSearchData()
    }
    //endregion

    //region Helper Functions
    fun init() {

        tvClearHistory.setOnClickListener(this)
        ivClose.setOnClickListener(this)
        cvClose.setOnClickListener(this)

        etSearch.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                var searchValue = etSearch.text.toString()
                if (!searchValue.isNullOrEmpty()) {
                    search(searchValue)
                } else
                    Toast.makeText(this@SearchActivity, getString(R.string.search_message), Toast.LENGTH_SHORT).show()

            }

            true

        }
    }


    private fun clearHistory() {
        rvSearchResult.adapter = null
//        rvSearchResult.adapter.notifyDataSetChanged()
        searchList.clear()
        PreferenceController.getInstance(this@SearchActivity).setSearchPref(AppConstants.SEARCH_DATA, searchList)

    }

    private fun getSearchData() {
        try {
            searchList = PreferenceController.getInstance(this@SearchActivity).getSearchPref(AppConstants.SEARCH_DATA)!!
        } catch (ex: Exception) {
            ex.printStackTrace()
        }


        if (searchList != null) {
            rvSearchResult.adapter = SearchAdapter(this@SearchActivity, searchList)
            rvSearchResult.layoutManager = LinearLayoutManager(AppController.getContext(), LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun search(searchValue: String) {
        startActivity(Intent(this, SearchResultActivity::class.java).putExtra(AppConstants.SEARCH_DATA, searchValue))
    }


    //endregion


}
