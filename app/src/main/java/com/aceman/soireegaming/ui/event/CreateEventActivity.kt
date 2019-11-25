package com.aceman.soireegaming.ui.event

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.location.Location
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.transition.TransitionManager
import com.aceman.soireegaming.R
import com.aceman.soireegaming.data.models.User
import com.aceman.soireegaming.data.models.UserChip
import com.aceman.soireegaming.utils.ChipsManager
import com.aceman.soireegaming.utils.Utils
import com.aceman.soireegaming.utils.base.BaseActivity
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.activity_create_event.*
import java.util.*


class CreateEventActivity(override val activityLayout: Int = R.layout.activity_create_event) :
    BaseActivity(),
    CreateEventActivityContract.CreateEventActivityViewInterface {
    private val mPresenter: CreateEventActivityPresenter = CreateEventActivityPresenter()
    lateinit var mPicture: String
    var mLocation: Location = Location("init")
    var itemPos: Int = -1
    var chipList: MutableList<UserChip> = mutableListOf<UserChip>()
    var beginDate = ""
    var endDate = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPresenter.attachView(this)
        setSupportActionBar(create_event_tb)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        mPresenter.getUserDataFromFirestore()
        datePicker()
        ChipsManager.initListOfChip(chipList)
        chipTest()

    }

    private fun datePicker() {

        date_event_picker.setOnClickListener {
            val cldr: Calendar = Calendar.getInstance()
            val day: Int = cldr.get(Calendar.DAY_OF_MONTH)
            val month: Int = cldr.get(Calendar.MONTH)
            val year: Int = cldr.get(Calendar.YEAR)
            val picker = DatePickerDialog(
                this,
                OnDateSetListener { _, year, monthOfYear, dayOfMonth -> date_event_picker.text =
                    dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year },
                year,
                month,
                day
            )
            picker.datePicker.minDate = System.currentTimeMillis() - 1000
            picker.show()
        }
        date_event_picker_2.setOnClickListener {
            beginDate = date_event_picker.text.toString()
            val cldr: Calendar = Calendar.getInstance()
            val day: Int = cldr.get(Calendar.DAY_OF_MONTH)
            val month: Int = cldr.get(Calendar.MONTH)
            val year: Int = cldr.get(Calendar.YEAR)
            val picker2 = DatePickerDialog(
                this,
                OnDateSetListener { _, year, monthOfYear, dayOfMonth -> date_event_picker_2.text =
                    dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year },
                year,
                month,
                day
            )
            picker2.datePicker.minDate = Utils.dateWithBSToMillis(beginDate)
            picker2.show()
        }
        endDate = date_event_picker_2.text.toString()
    }

    /**
     * Inflate the menu; this adds items to the action bar if it is present.
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.edit_profile_menu_tb, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            R.id.edit_profile_tb_validate -> {
/*                mPresenter.saveEventToFirebase(
                    EventInfos(
                        mPresenter.getCurrentUser()!!.uid,
                        Utils.todayDate,

                        create_event_name_et.text.toString(),
                        create_event_desc_et.text.toString(),
                        mPicture,
                        mLocation,
                        chipList,
                        private_event_spinner.selectedItemPosition,
                        gender_event_spinner.selectedItemPosition,
                        eat_event_spinner.selectedItemPosition,
                        sleep_event_spinner.selectedItemPosition)
                )*/
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    override fun updateUI(currentUser: User) {
        mPicture = currentUser.urlPicture.toString()
        mLocation.latitude = currentUser.userLocation.latitude
        mLocation.longitude = currentUser.userLocation.longitude

        Glide.with(this)
            .load(currentUser.urlPicture)
            .circleCrop()
            .into(create_event_profile_pic)
        create_event_location.setText(currentUser.userLocation.city)
    }

    fun chipTest() {

        val console = Utils.ListOfString.listOfConsole()
        val consoleAdapter = ArrayAdapter<String>(applicationContext,
            android.R.layout.simple_dropdown_item_1line, console
        )
        create_console_ac.setAdapter(consoleAdapter)
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
                        var chip: Chip = create_console_chipgroup.getChildAt(i) as Chip
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
                        var chip: Chip = create_style_chipgroup.getChildAt(j) as Chip
                        if (chip.text == selectedItem) {
                            Toast.makeText(
                                applicationContext, "Style $selectedItem déjà ajouté !",
                                Toast.LENGTH_SHORT
                            ).show()
                            clearAutocomplete()
                            break
                        }
                        if (j == create_style_chipgroup.childCount - 1) {
                            Toast.makeText(applicationContext, "Ajout $selectedItem",
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

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            hideKeyboard(create_console_ac)
            hideKeyboard(create_style_ac)
            clearAutocomplete()
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun clearAutocomplete() {
        create_console_ac.clearFocus()
        create_console_ac.text.clear()
        create_style_ac.clearFocus()
        create_style_ac.text.clear()
    }

    fun addChip(chipName: String, group: String) {
        // Initialize a new chip instance
        val chip = Chip(this)
        chip.text = chipName
        chip.tag = group
        when (chip.text) {
            "PS4", "PS3" -> chip.setChipBackgroundColorResource(R.color.playstation)
            "Xbox 360", "Xbox One" -> chip.setChipBackgroundColorResource(R.color.xbox)
            "PS2", "Xbox" -> chip.setChipBackgroundColorResource(android.R.color.black)
            "Dreamcast" -> chip.setChipBackgroundColorResource(R.color.dreamcast)
            "Gamecube" -> chip.setChipBackgroundColorResource(R.color.gamecube)
            "Wii", "Wii U", "3DS", "Switch" -> chip.setChipBackgroundColorResource(R.color.wii)
            "Android" ->  chip.setChipBackgroundColorResource(R.color.android)
            "PC" -> chip.setChipBackgroundColorResource(R.color.pc)
            "Autre" -> chip.setChipBackgroundColorResource(R.color.autre)
        }
        chip.setTextColor(resources.getColor(R.color.primaryTextColor))
        chip.isClickable = true
        chip.isCheckable = false
        chip.isCloseIconVisible = true

        saveToFirestore(chip)
    }

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
}
