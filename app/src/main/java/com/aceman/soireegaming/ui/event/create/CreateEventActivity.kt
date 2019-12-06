package com.aceman.soireegaming.ui.event.create

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isEmpty
import androidx.transition.TransitionManager
import com.aceman.soireegaming.BuildConfig
import com.aceman.soireegaming.R
import com.aceman.soireegaming.data.extensions.customTimeStamp
import com.aceman.soireegaming.data.extensions.hourSetting
import com.aceman.soireegaming.data.models.*
import com.aceman.soireegaming.ui.home.main.MainActivity
import com.aceman.soireegaming.utils.ChipsManager
import com.aceman.soireegaming.utils.Utils
import com.aceman.soireegaming.utils.base.BaseActivity
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.activity_create_event.*
import timber.log.Timber
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.math.absoluteValue

/**
 * Created by Lionel JOFFRAY - on 19/11/2019.
 *
 * Create Event is the Activity where user can create a new event.
 */
class CreateEventActivity(override val activityLayout: Int = R.layout.activity_create_event) :
    BaseActivity(),
    CreateEventActivityContract.CreateEventActivityViewInterface, OnMapReadyCallback {
    private val mPresenter: CreateEventActivityPresenter =
        CreateEventActivityPresenter()
    lateinit var mPicture: String
    private var mLocation = UserLocation(-1.0, -1.0, "City")
    private var itemPos: Int = -1
    private var chipList: MutableList<UserChip> = mutableListOf()
    private var dateList: MutableList<String> = mutableListOf("", "", "", "")
    private var eventList: MutableList<String> = mutableListOf()
    private var eventPlayers: MutableList<String> = mutableListOf()
    private var eventId: String = ""
    var placeLatLng: LatLng = LatLng(-1.0, -1.0)
    var dateMin = ""
    private var AUTOCOMPLETE_REQUEST_CODE = 100
    var fields: List<Place.Field> =
        listOf(Place.Field.LAT_LNG, Place.Field.NAME, Place.Field.ADDRESS)
    private lateinit var mMap: GoogleMap
    lateinit var placesClient: PlacesClient
    lateinit var mMarker: Marker
    /**
     * On create activity, initialise toolbar, places Client, configure the maps and get the user data
     * from Firebase. Also init the chip manager for adding chip.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPresenter.attachView(this)
        setSupportActionBar(create_event_tb)
        Places.initialize(applicationContext, BuildConfig.GOOGLE_MAPS_API)
        placesClient = Places.createClient(this)
        configureMaps()
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        mPresenter.getUserDataFromFirestore()
        datePicker()
        ChipsManager.initListOfChip(chipList)
        chipSetter()
        create_event_location.setOnClickListener {
            val intent = Autocomplete.IntentBuilder(
                AutocompleteActivityMode.OVERLAY, fields
            )
                .setCountry("FR")
                .setTypeFilter(TypeFilter.ADDRESS)
                .build(this)
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
        }

    }

    /**
     * Get the result of location changing ( Full address).
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            when (resultCode) {
                RESULT_OK -> {
                    mMap.clear()
                    val place = Autocomplete.getPlaceFromIntent(data!!)
                    Timber.i("%s%s", "%s, ", "Place: ${place.name} , Address: ${place.address}")
                    create_event_location.text = place.name
                    placeLatLng = place.latLng!!
                    mLocation.latitude = placeLatLng.latitude
                    mLocation.longitude = placeLatLng.longitude
                    mLocation.city = place.name!!
                    mMarker.title = place.name
                    mMarker.position = placeLatLng
                    moveMarker(mMarker)
                }
                AutocompleteActivity.RESULT_ERROR -> {
                    val status = Autocomplete.getStatusFromIntent(data!!)
                    Timber.i(status.statusMessage)
                }
                RESULT_CANCELED -> {
                    // The user canceled the operation.
                }
            }
        }
    }

    /**
     * Get the result of datepicker, with mindate lock to prevent false date.
     */
    private fun datePicker() {
        date_event_picker.setOnClickListener {
            val cldr: Calendar = Calendar.getInstance()
            val day: Int = cldr.get(Calendar.DAY_OF_MONTH)
            val month: Int = cldr.get(Calendar.MONTH)
            val year: Int = cldr.get(Calendar.YEAR)
            val picker = DatePickerDialog(
                this,
                OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                    date_event_picker.text =
                        dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year
                    dateMin = "$dayOfMonth/$monthOfYear/$year"
                    dateList[0] = date_event_picker.text.toString()
                },
                year,
                month,
                day
            )
            picker.datePicker.minDate = System.currentTimeMillis() - 1000
            picker.show()
        }
        date_event_picker_2.setOnClickListener {
            if (dateMin != "") {
                val cldr: Calendar = Calendar.getInstance()
                val day: Int = cldr.get(Calendar.DAY_OF_MONTH)
                val month: Int = cldr.get(Calendar.MONTH)
                val year: Int = cldr.get(Calendar.YEAR)
                val picker2 = DatePickerDialog(
                    this,
                    OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                        date_event_picker_2.text =
                            dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year
                        dateList[1] = date_event_picker_2.text.toString()
                    },
                    year,
                    month,
                    day
                )
                picker2.datePicker.minDate = Utils.dateWithBSToMillis(dateMin)
                picker2.show()

            } else
                Utils.snackBarPreset(
                    findViewById(android.R.id.content),
                    "Choissisez une date de début !"
                )
        }
        date_hour_picker.setOnClickListener {
            val cldr: Calendar = Calendar.getInstance()
            val hour: Int = cldr.get(Calendar.HOUR_OF_DAY)
            val minutes: Int = cldr.get(Calendar.MINUTE)
            val picker3 = TimePickerDialog(
                this,
                OnTimeSetListener { _, sHour, sMinute ->
                    date_hour_picker.text =
                        hourSetting(sHour, sMinute)
                    dateList[2] = date_hour_picker.text.toString()
                },
                hour,
                minutes,
                true
            )
            picker3.show()
        }

        date_hour_picker_2.setOnClickListener {
            val cldr: Calendar = Calendar.getInstance()
            val hour: Int = cldr.get(Calendar.HOUR_OF_DAY)
            val minutes: Int = cldr.get(Calendar.MINUTE)
            val picker4 = TimePickerDialog(
                this,
                OnTimeSetListener { _, sHour, sMinute ->
                    date_hour_picker_2.text =
                        hourSetting(sHour, sMinute)
                    dateList[3] = date_hour_picker_2.text.toString()
                },
                hour,
                minutes,
                true
            )
            picker4.show()
        }
    }

    /**
     * Inflate the menu; this adds items to the action bar if it is present.
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.edit_profile_menu_tb, menu)
        return super.onCreateOptionsMenu(menu)
    }

    /**
     * Save the event on click save event in Toolbar.
     */
    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            R.id.edit_profile_tb_validate -> {
                if (nullCheck()) {
                    eventId = customTimeStamp()
                    mPresenter.saveEventToFirebase(
                        EventInfos(
                            mPresenter.getCurrentUser()!!.uid,
                            mPresenter.getCurrentUser()!!.displayName!!,
                            eventId,
                            Utils.todayDate,
                            create_event_name_et.text.toString(),
                            create_event_desc_et.text.toString(),
                            mPicture,
                            mLocation,
                            chipList,
                            dateList,
                            eventPlayers,
                            EventMisc(
                                private_event_spinner.selectedItemPosition,
                                gender_event_spinner.selectedItemPosition,
                                eat_event_spinner.selectedItemPosition,
                                sleep_event_spinner.selectedItemPosition
                            )
                        )
                        , eventId
                    )
                    Utils.snackBarPreset(
                        findViewById(android.R.id.content),
                        "Event Programmé avec succès !"
                    )
                    Executors.newSingleThreadScheduledExecutor().schedule({
                        val intent = Intent(baseContext, MainActivity::class.java)
                        startActivity(intent)
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                    }, 2, TimeUnit.SECONDS)
                }


                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    /**
     * Check functions for empty informations on event.
     */
    private fun nullCheck(): Boolean {
        return when {
            create_event_name_et.text.toString() == "" -> {
                Utils.snackBarPreset(
                    findViewById(android.R.id.content),
                    "Il faut entrer un nom de soirée !"
                )
                false
            }
            create_event_desc_et.text.toString() == "" -> {
                Utils.snackBarPreset(
                    findViewById(android.R.id.content),
                    "Il faut entrer une description !"
                )
                false
            }
            mLocation.latitude == -1.0 -> {
                Utils.snackBarPreset(
                    findViewById(android.R.id.content),
                    "Il faut entrer une addresse !"
                )
                false
            }
            dateList.contains("") -> {
                Utils.snackBarPreset(
                    findViewById(android.R.id.content),
                    "Il faut entrer toutes les dates !"
                )
                false
            }
            create_console_chipgroup.isEmpty() -> {
                Utils.snackBarPreset(
                    findViewById(android.R.id.content),
                    "Il faut choisir au moins une console !"
                )
                false
            }
            create_style_chipgroup.isEmpty() -> {
                Utils.snackBarPreset(
                    findViewById(android.R.id.content),
                    "Il faut choisir au moins un style !"
                )
                false
            }
            else -> true
        }
    }

    /**
     * Update UI with user informations.
     */
    override fun updateUI(currentUser: User) {
        mPicture = currentUser.urlPicture.toString()
        mLocation = currentUser.userLocation
        mMarker.position = LatLng(mLocation.latitude, mLocation.longitude)
        mMarker.title = currentUser.userLocation.city
        moveMarker(mMarker)
        eventList = currentUser.eventList
        Glide.with(this)
            .load(currentUser.urlPicture)
            .circleCrop()
            .into(create_event_profile_pic)
        create_event_location.text = currentUser.userLocation.city
    }

    /**
     * Move the marker on location change.
     */
    override fun moveMarker(mMarker: Marker) {
        mMap.addMarker(
            MarkerOptions().position(mMarker.position)
                .title(mMarker.title)
        )
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mMarker.position))
        mMap.cameraPosition.zoom.absoluteValue
    }

    /**
     * Set the chip to chipgroup on user selection.
     */
    override fun chipSetter() {

        val console = Utils.ListOfString.listOfConsole()
        val consoleAdapter = ArrayAdapter<String>(
            applicationContext,
            android.R.layout.simple_dropdown_item_1line, console
        )
        create_console_ac.setAdapter(consoleAdapter)
        if (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.LOLLIPOP)
            create_console_ac.setDropDownBackgroundDrawable(ColorDrawable(resources.getColor(R.color.primaryLightColor)))
        create_console_ac.threshold = 1
        create_console_ac.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                val selectedItem = parent.getItemAtPosition(position).toString()
                var i = 0
                if (create_console_chipgroup.childCount == 0) {
                    Toast.makeText(applicationContext, "Ajout $selectedItem", Toast.LENGTH_SHORT)
                        .show()
                    addChip(selectedItem, "Console")
                } else {
                    while (i < create_console_chipgroup.childCount) {
                        val chip: Chip = create_console_chipgroup.getChildAt(i) as Chip
                        if (chip.text == selectedItem) {
                            Toast.makeText(
                                applicationContext, "Console $selectedItem déjà ajoutée !",
                                Toast.LENGTH_SHORT
                            ).show()
                            clearAutocomplete()
                            break
                        }
                        if (i == create_console_chipgroup.childCount - 1) {
                            Toast.makeText(
                                applicationContext, "Ajout $selectedItem", Toast.LENGTH_SHORT
                            ).show()
                            itemPos = i
                            addChip(selectedItem, "Console")
                            break
                        }
                        i++
                    }
                }
            }
        create_console_ac.onFocusChangeListener = View.OnFocusChangeListener { view, b ->
            if (b) {
                create_console_ac.showDropDown()
            }
        }

        val style = Utils.ListOfString.listOfStyle()
        val styleAdapter = ArrayAdapter<String>(
            applicationContext,
            android.R.layout.simple_dropdown_item_1line,
            style
        )
        create_style_ac.setAdapter(styleAdapter)
        create_style_ac.threshold = 1
        if (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.LOLLIPOP)
            create_style_ac.setDropDownBackgroundDrawable(ColorDrawable(resources.getColor(R.color.primaryLightColor)))
        create_style_ac.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                val selectedItem = parent.getItemAtPosition(position).toString()
                var j = 0
                if (create_style_chipgroup.childCount == 0) {
                    Toast.makeText(applicationContext, "Ajout $selectedItem", Toast.LENGTH_SHORT)
                        .show()
                    addChip(selectedItem, "Style")
                } else {
                    while (j < create_style_chipgroup.childCount) {
                        val chip: Chip = create_style_chipgroup.getChildAt(j) as Chip
                        if (chip.text == selectedItem) {
                            Toast.makeText(
                                applicationContext, "Style $selectedItem déjà ajouté !",
                                Toast.LENGTH_SHORT
                            ).show()
                            clearAutocomplete()
                            break
                        }
                        if (j == create_style_chipgroup.childCount - 1) {
                            Toast.makeText(
                                applicationContext, "Ajout $selectedItem",
                                Toast.LENGTH_SHORT
                            ).show()
                            addChip(selectedItem, "Style")
                            break
                        }
                        j++
                    }
                }
            }
        create_style_ac.onFocusChangeListener = View.OnFocusChangeListener { view, b ->
            if (b) {
                create_style_ac.showDropDown()
            }
        }
    }

    /**
     * Hide keyboard on autocomplete clear.
     */
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            hideKeyboard(create_console_ac)
            hideKeyboard(create_style_ac)
            clearAutocomplete()
        }
        return super.dispatchTouchEvent(ev)
    }

    /**
     * Clear autocomplete hint.
     */
    override fun clearAutocomplete() {
        create_console_ac.clearFocus()
        create_console_ac.text.clear()
        create_style_ac.clearFocus()
        create_style_ac.text.clear()
    }

    /**
     * Function to add chip.
     */
    override fun addChip(chipName: String, group: String) {
        // Initialize a new chip instance
        val chip = Chip(this)
        chip.text = chipName
        chip.tag = group
        chip.setChipBackgroundColorResource(Utils.chipColor(chip))
        chip.setTextColor(resources.getColor(R.color.primaryTextColor))
        chip.isClickable = true
        chip.isCheckable = false
        chip.isCloseIconVisible = true

        saveToFirestore(chip)
    }

    /**
     * Save the chip selection to firestore with a boolean check.
     */
    fun saveToFirestore(chip: Chip) {
        if (chip.tag == "Console") {
            chip.setOnCloseIconClickListener {
                TransitionManager.beginDelayedTransition(create_console_chipgroup)
                create_console_chipgroup.removeView(chip)
                for (item in chipList) {
                    if (item.name == chip.text) {
                        chipList[chipList.indexOf(item)].check = false
                        break
                    }
                }
                //  mPresenter.updateChip(chipList)
            }
            create_console_chipgroup.addView(chip)
            for (item in chipList) {
                if (item.name == chip.text) {
                    chipList[chipList.indexOf(item)].check = true
                    break
                }
            }
            //  mPresenter.updateChip(chipList)
            clearAutocomplete()
            hideKeyboard(create_console_ac)
        }

        if (chip.tag == "Style") {
            chip.setOnCloseIconClickListener {
                TransitionManager.beginDelayedTransition(create_style_chipgroup)
                create_style_chipgroup.removeView(chip)
                for (item in chipList) {
                    if (item.name == chip.text) {
                        chipList[chipList.indexOf(item)].check = false
                        break
                    }
                }
                //  mPresenter.updateChip(chipList)
            }
            create_style_chipgroup.addView(chip)
            for (item in chipList) {
                if (item.name == chip.text) {
                    chipList[chipList.indexOf(item)].check = true
                    break
                }
            }
            // mPresenter.updateChip(chipList)
            clearAutocomplete()
            hideKeyboard(create_style_ac)
        }
    }

    /**
     * Set the map.
     */
    fun configureMaps() {
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.address_map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    /**
     * Called when the map is ready to add all markers and objects to the map.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isMapToolbarEnabled = false
        val paris = LatLng(48.864716, 2.349014)
        mMarker = mMap.addMarker(
            MarkerOptions().position(paris)
                .title("Paris")
        )
        mMap.moveCamera(CameraUpdateFactory.newLatLng(paris))
        mMap.cameraPosition.zoom.absoluteValue
    }
}
