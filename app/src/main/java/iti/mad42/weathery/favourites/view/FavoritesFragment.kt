package iti.mad42.weathery.favourites.view

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import iti.mad42.weathery.R
import iti.mad42.weathery.databinding.FragmentFavoritesBinding
import iti.mad42.weathery.favoriteweatherdetails.view.FavoriteWeatherDetailsActivity
import iti.mad42.weathery.favourites.viewmodel.FavoritesViewModel
import iti.mad42.weathery.favourites.viewmodel.FavoritesViewModelFactory
import iti.mad42.weathery.model.db.ConcreteLocalDataSource
import iti.mad42.weathery.model.network.RemoteDataSource
import iti.mad42.weathery.model.pojo.FavoriteWeather
import iti.mad42.weathery.model.pojo.Repository
import iti.mad42.weathery.model.pojo.TodayHoursTemp
import iti.mad42.weathery.model.pojo.Utility


class FavoritesFragment : Fragment() , OnClickFavPlaceListener {
    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var favoritesAdapter : FavoriteLocationAdapter
    lateinit var favPlaceFactory: FavoritesViewModelFactory
    lateinit var favPlaceVM : FavoritesViewModel
    //lateinit var favList : ArrayList<TodayHoursTemp>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        var view : View = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFavRecycler()
        initFavFactoryAndViewModel()
        getFavPlaces()
        if(isGoogleServiceOK()){
            goToMapActivity()
        }

    }

    fun initFavRecycler(){
        binding.favRecycler
        favoritesAdapter = FavoriteLocationAdapter(listOf<FavoriteWeather>(), context, this)
        binding.favRecycler.setHasFixedSize(true)
        binding.favRecycler.apply {
            this.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            this.adapter = favoritesAdapter
        }
        binding.apply {
            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT){
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val favPlace = favoritesAdapter.favLocationList[viewHolder.adapterPosition]

                    var confirmDialog = AlertDialog.Builder(requireContext())
                    confirmDialog.setMessage(getString(R.string.delete_fav_msg))
                        .setCancelable(true)
                        .setPositiveButton(getString(R.string.delete_btn_txt), DialogInterface.OnClickListener{
                                dialogInterface, i ->
                            favPlaceVM.deletePlaceFromFav(favPlace)
                        })
                        .setNegativeButton(getString(R.string.cancel_perm_txt), DialogInterface.OnClickListener{
                                dialogInterface, i ->
                            favoritesAdapter.notifyDataSetChanged()
                            dialogInterface.cancel()

                        })
                    val alert = confirmDialog.create()
                    alert.setTitle(getString(R.string.confirm_delete_txt))
                    alert.show()
                }

            }).attachToRecyclerView(favRecycler)

        }

    }

    private fun initFavFactoryAndViewModel(){
        favPlaceFactory = FavoritesViewModelFactory(Repository.getInstance(RemoteDataSource.getInstance(), ConcreteLocalDataSource(requireContext()), requireContext()))
        favPlaceVM = ViewModelProvider(this,favPlaceFactory).get(FavoritesViewModel::class.java)
    }

    fun getFavPlaces(){
        favPlaceVM.getFavPlaces().observe(viewLifecycleOwner){favPlaces ->
            if(favPlaces.isNotEmpty()){
                favoritesAdapter.favLocationList = favPlaces
                favoritesAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onClickFavPlace(favPlace: FavoriteWeather) {
        goToFavPlaceDetails(favPlace)
    }
    fun goToFavPlaceDetails(favPlace : FavoriteWeather){
        var goToFavDetails : Intent = Intent(activity, FavoriteWeatherDetailsActivity::class.java)
        goToFavDetails.putExtra("fromFavToDetails", favPlace)
        startActivity(goToFavDetails)
    }

    //check connectivity in utility

    private fun isGoogleServiceOK() : Boolean{
        var available : Int = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(requireActivity())
        when{
            available == ConnectionResult.SUCCESS -> return true
            GoogleApiAvailability.getInstance().isUserResolvableError(available) -> {
                var dialog : Dialog? = GoogleApiAvailability.getInstance().getErrorDialog(this, available, Utility.ERROR_DIALOG_REQUEST)
                dialog?.show()
            }
            else -> Toast.makeText(requireContext(), getString(R.string.refuse_goto_map_txt), Toast.LENGTH_SHORT).show()
        }
        return false
//        if(available == ConnectionResult.SUCCESS){
//            return true
//        }else{
//
//        }
    }

    fun goToMapActivity(){
        binding.addFavBtn.setOnClickListener {
            var goToMap : Intent = Intent(requireActivity(), MapActivity::class.java)
            goToMap.putExtra("isFav", true)
            startActivity(goToMap)
        }
    }

}