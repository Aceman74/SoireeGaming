package com.aceman.soireegaming.ui.event

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Intent
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
import com.aceman.soireegaming.R
import com.aceman.soireegaming.data.extensions.customTimeStamp
import com.aceman.soireegaming.data.extensions.hourSetting
import com.aceman.soireegaming.data.models.*
import com.aceman.soireegaming.ui.bottomnavigation.home.MainActivity
import com.aceman.soireegaming.utils.ChipsManager
import com.aceman.soireegaming.utils.Utils
import com.aceman.soireegaming.utils.base.BaseActivity
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.activity_create_event.*
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


class CreateEventActivity(override val activityLayout: Int = R.layout.activity_create_event) :
    BaseActivity(),
    CreateEventActivityContract.CreateEventActivityViewInterface {
    private val mPresenter: CreateEventActivityPresenter = CreateEventActivityPresenter()
    lateinit var mPicture: String
   private var mLocation = UserLocation(-1.0,-1.0,"City")
   private var itemPos: Int = -1
   private var chipList: MutableList<UserChip> = mutableListOf<UserChip>()
   private var dateList: MutableList<String> = mutableListOf("","","","")
   private var eventList: MutableList<String> = mutableListOf()
   private var eventPlayers: MutableList<String> = mutableListOf()
   private var eventId : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPresenter.attachView(this)
        setSupportActionBar(create_event_tb)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        mPresenter.getUserDataFromFirestore()
        datePicker()
        ChipsManager.initListOfChip(chipList)
        chipSetter()

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
                    dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year
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
            val cldr: Calendar = Calendar.getInstance()
            val day: Int = cldr.get(Calendar.DAY_OF_MONTH)
            val month: Int = cldr.get(Calendar.MONTH)
            val year: Int = cldr.get(Calendar.YEAR)
            val picker2 = DatePickerDialog(
                this,
                OnDateSetListener { _, year, monthOfYear, dayOfMonth -> date_event_picker_2.text =
                    dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year
                    dateList[1] = date_event_picker_2.text.toString()
                },
                year,
                month,
                day
            )
            picker2.datePicker.minDate = Utils.dateWithBSToMillis(dateList[0])
            picker2.show()
        }
        date_hour_picker.setOnClickListener {
            val cldr: Calendar = Calendar.getInstance()
            val hour: Int = cldr.get(Calendar.HOUR_OF_DAY)
            val minutes: Int = cldr.get(Calendar.MINUTE)
            val picker3 = TimePickerDialog(
                this,
                OnTimeSetListener { _, sHour, sMinute -> date_hour_picker.text = String().hourSetting(sHour,sMinute)
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
                OnTimeSetListener { _, sHour, sMinute -> date_hour_picker_2.text = String().hourSetting(sHour,sMinute)
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

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            R.id.edit_profile_tb_validate -> {
                if(nullCheck()){
                eventId = String().customTimeStamp()
                    eventList.add(eventId)
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
                            sleep_event_spinner.selectedItemPosition)
                        )
                ,eventId
                )
                    mPresenter.addEventToUserList(eventList)
                    Utils.snackBarPreset(findViewById(android.R.id.content),"Event Programmé avec succès !")
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

    private fun nullCheck(): Boolean {
        return when{
            create_event_name_et.text.toString() == "" ->{
                Utils.snackBarPreset(findViewById(android.R.id.content),"Il faut entrer un nom de soirée !")
                false
            }
            create_event_desc_et.text.toString() == "" ->{Utils.snackBarPreset(findViewById(android.R.id.content),"Il faut entrer une description !")
                false
            }
            dateList.contains("") -> {Utils.snackBarPreset(findViewById(android.R.id.content),"Il faut entrer toutes les dates !")
                false
            }
            create_console_chipgroup.isEmpty() ->{Utils.snackBarPreset(findViewById(android.R.id.content),"Il faut choisir au moins une console !")
                false
            }
            create_style_chipgroup.isEmpty() -> {Utils.snackBarPreset(findViewById(android.R.id.content),"Il faut choisir au moins un style !")
                false
            }
            else -> true
        }
    }

    override fun updateUI(currentUser: User) {
        mPicture = currentUser.urlPicture.toString()
        mLocation = currentUser.userLocation
        eventList = currentUser.eventList
        Glide.with(this)
            .load(currentUser.urlPicture)
            .circleCrop()
            .into(create_event_profile_pic)
        create_event_location.setText(currentUser.userLocation.city)
    }

    fun chipSetter() {

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
        chip.setChipBackgroundColorResource(Utils.chipColor(chip))
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
