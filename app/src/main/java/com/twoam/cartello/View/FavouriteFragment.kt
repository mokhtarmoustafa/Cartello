package com.twoam.cartello.View


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.twoam.Networking.INetworkCallBack
import com.twoam.Networking.NetworkManager
import com.twoam.cartello.Model.Product
import com.twoam.cartello.R
import com.twoam.cartello.Utilities.API.ApiResponse
import com.twoam.cartello.Utilities.API.ApiServices
import com.twoam.cartello.Utilities.Adapters.FavouriteAdapter
import com.twoam.cartello.Utilities.Base.BaseFragment
import com.twoam.cartello.Utilities.General.*


/**
 * A simple [Fragment] subclass.
 */
class FavouriteFragment : BaseFragment(), IProductFavouritesCallback,IBottomSheetCallback {

    //region Members
    private lateinit var currentView: View
    private var rvFavourites: RecyclerView? = null
    private var tvEmptyDataFavouriteFavourite: TextView? = null
    private var tvTotalFavourites: TextView? = null
    private var adapter: FavouriteAdapter? = null
    private lateinit var listener: IBottomSheetCallback
    private lateinit var ivBackFavourite: ImageView
    private lateinit var favoritesList: ArrayList<Product>
    //endregion

    //region Events


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        currentView = inflater.inflate(R.layout.fragment_favourite, container, false)

        init()
        getFavourites()
        return currentView
    }



    //called when the view hidden state changed
    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden)
            getFavourites()
    }

    override fun onAddToFavourite(product: Product) {
        addToFavourites(product)
    }

    override fun onRemoveFromFavourite(product: Product) {
        removeFromFavourites(product)
    }

    override fun getSimilarProducts(productId: Int) {

    }

    override fun onBottomSheetClosed(isClosed: Boolean) {

    }

    override fun onBottomSheetSelectedItem(index: Int) {
        if(index==9)//p[en product details view
            listener.onBottomSheetSelectedItem(9)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is IBottomSheetCallback) {
            listener = context
        } else {
            throw ClassCastException(context.toString() + " must implement IBottomSheetCallback.onBottomSheetSelectedItem")
        }
    }




    //endregion

    //region Helper Functions
    private fun getFavourites() {
        showDialogue()
        if (NetworkManager().isNetworkAvailable(context!!)) {
            var token = AppConstants.BEARER + AppConstants.CurrentLoginUser.token
            var request = NetworkManager().create(ApiServices::class.java)
            var endPoint = request.getFavourites(token)
            NetworkManager().request(endPoint, object : INetworkCallBack<ApiResponse<ArrayList<Product>>> {
                override fun onFailed(error: String) {
                    hideDialogue()
                    showAlertDialouge(error)
                }

                override fun onSuccess(response: ApiResponse<ArrayList<Product>>) {
                    if (response.code == AppConstants.CODE_200) {
                        favoritesList = response.data!!
                        prepareFavouritesData(response.data!!)
                        hideDialogue()
                    } else {
                        hideDialogue()
                        showAlertDialouge(getString(R.string.error_network))
                    }
                }
            })
        } else {
            hideDialogue()
            showAlertDialouge(getString(R.string.error_no_internet))
        }
    }

    private fun addToFavourites(product: Product) {
        showDialogue()
        if (NetworkManager().isNetworkAvailable(context!!)) {
            var token = AppConstants.BEARER + AppConstants.CurrentLoginUser.token
            var request = NetworkManager().create(ApiServices::class.java)
            var endPoint = request.addToFavourite(token, product.id)
            NetworkManager().request(endPoint, object : INetworkCallBack<ApiResponse<Boolean>> {
                override fun onFailed(error: String) {
                    hideDialogue()
                    showAlertDialouge(error)
                }

                override fun onSuccess(response: ApiResponse<Boolean>) {
                    if (response.code == AppConstants.CODE_200) {
                        //refresh data
                        product.is_favourite = true
                        favoritesList.add(product)
                        prepareFavouritesData(favoritesList)
                        hideDialogue()
//                        adapter?.notifyDataSetChanged()
                    } else {
                        hideDialogue()
                        showAlertDialouge(getString(R.string.error_network))
                    }
                }
            })
        } else {
            hideDialogue()
            showAlertDialouge(getString(R.string.error_no_internet))
        }
    }

    private fun removeFromFavourites(product: Product) {
        showDialogue()
        if (NetworkManager().isNetworkAvailable(context!!)) {
            var token = AppConstants.BEARER + AppConstants.CurrentLoginUser.token
            var request = NetworkManager().create(ApiServices::class.java)
            var endPoint = request.removeFromFavourite(token, product.id)
            NetworkManager().request(endPoint, object : INetworkCallBack<ApiResponse<Boolean>> {
                override fun onFailed(error: String) {
                    hideDialogue()
                    showAlertDialouge(error)
                }

                override fun onSuccess(response: ApiResponse<Boolean>) {
                    if (response.code == AppConstants.CODE_200) {
                        //refresh data
                        favoritesList.remove(product)
                        prepareFavouritesData(favoritesList)
                        hideDialogue()
                    } else {
                        hideDialogue()
                        showAlertDialouge(getString(R.string.error_network))
                    }
                }
            })
        } else {
            hideDialogue()
            showAlertDialouge(getString(R.string.error_no_internet))
        }
    }

    private fun prepareFavouritesData(products: ArrayList<Product>) {
        if (products.size > 0) {
            rvFavourites?.visibility = View.VISIBLE
            tvEmptyDataFavouriteFavourite?.visibility = View.INVISIBLE
            tvTotalFavourites?.text = products.size.toString()

            adapter = FavouriteAdapter(context!!, products, this,this)
            rvFavourites?.adapter = adapter
            rvFavourites?.layoutManager = GridLayoutManager(AppController.getContext(), 2, GridLayoutManager.VERTICAL, false)
        } else {

            rvFavourites?.visibility = View.INVISIBLE
            tvEmptyDataFavouriteFavourite?.visibility = View.VISIBLE
            tvTotalFavourites?.text = products.size.toString()

        }


    }

    private fun init() {
        rvFavourites = currentView.findViewById(R.id.rvFavourites)
        tvEmptyDataFavouriteFavourite = currentView.findViewById(R.id.tvEmptyDataFavouriteFavourite)
        tvTotalFavourites = currentView.findViewById(R.id.tvTotalFavourites)
        ivBackFavourite = currentView.findViewById(R.id.ivBackFavourite)

        ivBackFavourite.setOnClickListener {
            listener.onBottomSheetSelectedItem(6)
        }
    }
    //endregion


}
