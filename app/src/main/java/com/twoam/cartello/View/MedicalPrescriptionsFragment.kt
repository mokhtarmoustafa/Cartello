package com.twoam.cartello.View

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.twoam.Networking.INetworkCallBack
import com.twoam.Networking.NetworkManager
import com.twoam.cartello.Model.MedicalPrescriptions
import com.twoam.cartello.R
import com.twoam.cartello.Utilities.API.ApiResponse
import com.twoam.cartello.Utilities.API.ApiServices
import com.twoam.cartello.Utilities.Adapters.MedicalAdapter
import com.twoam.cartello.Utilities.Base.BaseFragment
import com.twoam.cartello.Utilities.General.AppConstants
import com.twoam.cartello.Utilities.General.AppController
import com.twoam.cartello.Utilities.General.IBottomSheetCallback
import com.twoam.cartello.Utilities.General.MedicalBottomSheetDialog
import kotlinx.android.synthetic.main.fragment_medical_prescriptions.*
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView

import android.widget.ImageView


class MedicalPrescriptionsFragment : BaseFragment(), IBottomSheetCallback {


    //region MEMBERS
    private lateinit var currentView: View
    private var medicalList = ArrayList<MedicalPrescriptions>()
    private var bottomSheet = MedicalBottomSheetDialog()
    private lateinit var btnAddMedical: Button
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var rvMedical: RecyclerView
    private var ivBackMedical: ImageView? = null


    //endregion
    //region EVENTS
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        currentView = inflater.inflate(R.layout.fragment_medical_prescriptions, container, false)

        init()

        getAllMedical()
        return currentView
    }

    override fun onResume() {
        super.onResume()
        //this called once a new medical is added successfully on Product Details ACtivity
        //so to update the view get all data
        getAllMedical()
    }

    override fun onBottomSheetClosed(isClosed: Boolean) {

    }

    override fun onBottomSheetSelectedItem(index: Int) {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    //endregion
    //region HELPER FUNCTIONS
    private fun init() {
        btnAddMedical = currentView.findViewById(R.id.btnAddMedical)
        swipeRefresh = currentView.findViewById(R.id.swipeRefresh)
        rvMedical = currentView.findViewById(R.id.rvMedical)
        ivBackMedical=currentView?.findViewById(R.id.ivBackMedical)

        rvMedical.isNestedScrollingEnabled = false



        btnAddMedical.setOnClickListener(View.OnClickListener {
            bottomSheet.Action = 1
            bottomSheet.show(fragmentManager, "Custom Bottom Sheet")
        })


        swipeRefresh.setOnRefreshListener {
            refreshData()
        }
    }

    private fun refreshData() {
        getAllMedical()
        swipeRefresh.isRefreshing = false
    }

    private fun getAllMedical(): ArrayList<MedicalPrescriptions> {
        showDialogue()
        if (NetworkManager().isNetworkAvailable(AppController.getContext())) {
            var token = AppConstants.BEARER + AppConstants.CurrentLoginUser.token
            var request = NetworkManager().create(ApiServices::class.java)
            var endPoint = request.getAllMedicalPrescriptions(token)
            NetworkManager().request(endPoint, object : INetworkCallBack<ApiResponse<ArrayList<MedicalPrescriptions>>> {
                override fun onFailed(error: String) {
                    hideDialogue()
                    showAlertDialouge(error)
                }

                override fun onSuccess(response: ApiResponse<ArrayList<MedicalPrescriptions>>) {
                    if (response.code == AppConstants.CODE_200) {
                        medicalList = response.data!!
                        tvTotalMedical.text = medicalList.size.toString()
                        prepareMedicalData(medicalList)
                        hideDialogue()
                    } else {
                        hideDialogue()
                        Toast.makeText(AppController.getContext(), response.message, Toast.LENGTH_SHORT).show()
                    }

                }
            })
        } else {
            hideDialogue()
            showAlertDialouge(getString(R.string.error_no_internet))
        }
        return medicalList
    }

    private fun prepareMedicalData(medicalList: ArrayList<MedicalPrescriptions>) {

        var adapter = MedicalAdapter(fragmentManager, AppController.getContext(), medicalList)
        rvMedical.adapter = adapter
        rvMedical.layoutManager = LinearLayoutManager(AppController.getContext(), LinearLayoutManager.VERTICAL, false)

    }


    //endregion


}
