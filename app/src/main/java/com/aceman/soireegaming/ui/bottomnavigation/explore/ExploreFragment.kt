package com.aceman.soireegaming.ui.bottomnavigation.explore


import android.app.DatePickerDialog
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aceman.soireegaming.R
import com.aceman.soireegaming.data.models.EventInfos
import com.aceman.soireegaming.data.models.User
import com.aceman.soireegaming.ui.adapters.explore.ExploreAdapter
import com.aceman.soireegaming.ui.event.detail.EventDetailActivity
import com.aceman.soireegaming.ui.profile.ProfileActivity
import com.aceman.soireegaming.utils.Utils
import com.aceman.soireegaming.utils.base.BaseView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_explore.*
import timber.log.Timber
import java.util.*
import kotlin.Comparator
import kotlin.math.absoluteValue
import kotlin.math.roundToInt


/**
 * A simple [Fragment] subclass.
 */
class ExploreFragment : Fragment(), BaseView, ExploreContract.ExploreViewInterface,
    OnMapReadyCallback {
    private val mPresenter: ExplorePresenter = ExplorePresenter()
    private lateinit var mMap: GoogleMap
    lateinit var mMarker: Marker
    var mUser = User()
    var mEventList = mutableListOf<String>()
    var mEventsDetailsList = mutableListOf<EventInfos>()
    lateinit var mRecyclerView: RecyclerView
    var mDate = ""
    var mFilteredList = mutableListOf<EventInfos>()
    lateinit var userLoc: LatLng

    companion object {
        fun newInstance(): ExploreFragment {
            return ExploreFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        val mView = inflater.inflate(R.layout.fragment_explore, container, false)
        mPresenter.attachView(this)
        return mView
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(explore_tb)
        configureMaps()
        mPresenter.getUserDataFromFirestore()
        search_btn.setOnClickListener { onSearchClick() }
    }


    private fun onSearchClick() {
        filterSearch()
    }

    private fun filterSearch() {
        var food = explore_food_tv.selectedItemPosition
        var sleep = explore_sleep_tv.selectedItemPosition
        var gender = explore_gender_tv.selectedItemPosition
        var console = explore_console_tv.selectedItemPosition
        var distance = explore_distance_tv.selectedItemPosition
        var stringConsole = resources.getStringArray(R.array.console)[console]
        when (distance) {
            1 -> distance = 5
            2 -> distance = 10
            3 -> distance = 15
            4 -> distance = 20
            5 -> distance = 25
        }
        explore_date_tv.setOnClickListener {
            val cldr: Calendar = Calendar.getInstance()
            val day: Int = cldr.get(Calendar.DAY_OF_MONTH)
            val month: Int = cldr.get(Calendar.MONTH)
            val year: Int = cldr.get(Calendar.YEAR)
            val picker = DatePickerDialog(
                requireContext(),
                DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                    explore_date_tv.text =
                        dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year
                    mDate = explore_date_tv.text.toString()
                },
                year,
                month,
                day
            )
            picker.datePicker.minDate = System.currentTimeMillis() - 1000
            picker.show()
        }
        if (food == 0 && sleep == 0 && gender == 0 && console == 0 && mDate == "" && distance == 0) {
            mFilteredList.clear()
            mFilteredList.addAll(mEventsDetailsList)
            configureRecyclerView()
            sortByDate()
        } else {
            mFilteredList.clear()
            mFilteredList.addAll(mEventsDetailsList)
            var userLocation = Location("user")
            userLocation.latitude = mUser.userLocation.latitude
            userLocation.longitude = mUser.userLocation.longitude

            var eventLocation = Location("event")

            var i = 0
            for (item in mEventsDetailsList) {
                eventLocation.latitude = item.location.latitude
                eventLocation.longitude = item.location.longitude
                var distanceEvent = userLocation.distanceTo(eventLocation).roundToInt() / 1000
                when {
                    mDate != "" && Utils.dateWithBSToMillis(item.dateList[0]) > Utils.dateWithBSToMillis(
                        mDate
                    ) -> {
                        mFilteredList.removeAt(mFilteredList.indexOf(item))
                    }
                    item.eventMisc.eat != food && food != 0 -> {
                        mFilteredList.removeAt(mFilteredList.indexOf(item))
                    }
                    item.eventMisc.sleep != sleep && sleep != 0 -> {
                        mFilteredList.removeAt(mFilteredList.indexOf(item))
                    }
                    item.eventMisc.gender != gender && gender != 0 -> {
                        mFilteredList.removeAt(mFilteredList.indexOf(item))
                    }
                    distanceEvent > distance && distance != 0 -> {
                        mFilteredList.removeAt(mFilteredList.indexOf(item))
                    }
                    stringConsole != "Toutes" -> {
                        for (chip in item.chipList) {
                            if (stringConsole == chip.name && !chip.check)
                                mFilteredList.removeAt(mFilteredList.indexOf(item))
                        }
                    }
                }
            }
            configureRecyclerView()
            sortByDate()
        }

    }

    /**
     * Set the map.
     */
    fun configureMaps() {
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.address_map_explore) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    /**
     * Called when the map is ready to add all markers and objects to the map.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isMapToolbarEnabled = false
        val Paris = LatLng(48.864716, 2.349014)
        mMarker = mMap.addMarker(
            MarkerOptions().position(Paris)
                .title("Paris")
        )
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Paris))
        mMap.cameraPosition.zoom.absoluteValue
    }

    /**
     * Initialize the recyclerview for picture preview.
     */
    fun configureRecyclerView() {
        mRecyclerView = explore_rv
        mRecyclerView.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        mRecyclerView.adapter = ExploreAdapter(mFilteredList) {
            Timber.tag("All Events RV click").i(it)
            if (it.length < 9)
                launchEventDetailActivity(it)
            else
                launchProfileDetailActivity(it)
        }
        configureMarkers(mFilteredList)
    }

    private fun configureMarkers(mFilteredList: MutableList<EventInfos>) {
        mMap.clear()
        mMarker = mMap.addMarker(
            MarkerOptions().position(userLoc)
                .title("Tu es ici !")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_user))
        )
        mMarker.showInfoWindow()

        for (item in mFilteredList) {
            var itemLoc = LatLng(item.location.latitude, item.location.longitude)
            mMap.addMarker(
                MarkerOptions()
                    .position(itemLoc)
                    .title(item.title)
            )

        }
    }

    private fun launchProfileDetailActivity(uid: String) {
        val intent = Intent(requireContext(), ProfileActivity::class.java)
        intent.putExtra("uid", uid)
        startActivity(intent)
    }

    private fun launchEventDetailActivity(eid: String) {
        val intent = Intent(requireContext(), EventDetailActivity::class.java)
        intent.putExtra("eid", eid)
        startActivity(intent)
    }

    override fun updateUI(currentUser: User) {
        mUser = currentUser
        userLoc = LatLng(mUser.userLocation.latitude, mUser.userLocation.longitude)
        mMarker = mMap.addMarker(
            MarkerOptions().position(userLoc)
                .title("Tu es ici !")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_user))
        )
        mMarker.showInfoWindow()
        mMap.moveCamera(CameraUpdateFactory.newLatLng(userLoc))
        mMap.cameraPosition.zoom.absoluteValue
        mPresenter.getAllEvents()
    }

    override fun updateEventList(list: MutableList<String>) {
        mEventList = list
        for (event in list) {
            mPresenter.addEventInfos(event)
        }
    }

    override fun updateEvents(event: EventInfos) {
        mEventsDetailsList.add(event)
    }

    fun sortByDate() {
        mEventsDetailsList.sortWith(Comparator { o1, o2 -> o1.dateList[0].compareTo(o2.dateList[0]) })
        mEventsDetailsList.reverse()
        mRecyclerView.adapter!!.notifyDataSetChanged()
    }

}
