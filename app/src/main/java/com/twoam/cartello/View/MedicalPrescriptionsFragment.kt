package com.twoam.cartello.View


import android.app.FragmentManager
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


class MedicalPrescriptionsFragment : BaseFragment(),IBottomSheetCallback {


    //region MEMBERS
    private lateinit var currentView: View
    private lateinit var medicalList: ArrayList<MedicalPrescriptions>
    private var bottomSheet = MedicalBottomSheetDialog()
    private lateinit var btnAddMedical: Button


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

    override fun onBottomSheetClosed(isClosed: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBottomSheetSelectedItem(index: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    //endregion
    //region HELPER FUNCTIONS
    private fun init() {
        btnAddMedical = currentView.findViewById(R.id.btnAddMedical)

        btnAddMedical.setOnClickListener(View.OnClickListener {
            bottomSheet.Action = 1
            bottomSheet.show(fragmentManager, "Custom Bottom Sheet")
        })
    }

    private fun getAllMedical(): ArrayList<MedicalPrescriptions> {
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
                        prepareMedicalData(medicalList)
                    } else {
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

        var adapter = MedicalAdapter(AppController.getContext(), medicalList)
        rvMedical.adapter = adapter
        rvMedical.layoutManager = LinearLayoutManager(AppController.getContext(), LinearLayoutManager.VERTICAL, false)
    }

    fun addMedical(token: String, name: String, note: String, image: String) {

    }

    fun loadMedicalDetails()
    {}
    //endregion


}
