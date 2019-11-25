package com.aceman.soireegaming.utils

import android.R
import android.app.Application
import android.content.Context
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.transition.TransitionManager
import com.aceman.soireegaming.data.models.UserChip
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlinx.android.synthetic.main.activity_profile.*

/**
 * Created by Lionel JOFFRAY - on 25/11/2019.
 */

class ChipsManager {
    companion object{

         fun initListOfChip(chipList: MutableList<UserChip>) {
            val chipsConsole: Array<String> = Utils.ListOfString.listOfConsole()
            val chipStyle : Array<String> = Utils.ListOfString.listOfStyle()
            var i = 0
            var j = 0
            for(item in chipsConsole){
                chipList.add(i, UserChip(chipsConsole[i],"Console",false))
                i++
            }
            for(item in chipStyle){
                chipList.add(i, UserChip(chipStyle[j],"Style",false))
                j++
                i++
            }
        }

    fun chipTest(applicationContext: Context, console_ac: AutoCompleteTextView, style_ac: AutoCompleteTextView,
                 profile_console_chipgroup: ChipGroup, style_chipgroup: ChipGroup, itemPos: Int) {

        var item = itemPos
        val console = Utils.ListOfString.listOfConsole()
        val consoleAdapter = ArrayAdapter<String>(
            applicationContext,
            R.layout.simple_dropdown_item_1line,
            console
        )
        console_ac.setAdapter(consoleAdapter)
        console_ac.threshold = 1
        console_ac.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                val selectedItem = parent.getItemAtPosition(position).toString()
                var i = 0
                if (profile_console_chipgroup.childCount == 0) {
                    Toast.makeText(applicationContext, "Ajout $selectedItem", Toast.LENGTH_SHORT)
                        .show()
                    addChip(selectedItem, "Console", applicationContext)
                } else {
                    while (i < profile_console_chipgroup.childCount) {
                        var chip: Chip = profile_console_chipgroup.getChildAt(i) as Chip
                        if (chip.text == selectedItem) {
                            Toast.makeText(
                                applicationContext,
                                "Console $selectedItem déjà ajoutée !",
                                Toast.LENGTH_SHORT
                            ).show()
                            clearAutocomplete(console_ac,style_ac)
                            break
                        }
                        if (i == profile_console_chipgroup.childCount - 1) {
                            Toast.makeText(
                                applicationContext,
                                "Ajout $selectedItem",
                                Toast.LENGTH_SHORT
                            ).show()
                            item = i
                            addChip(selectedItem, "Console",applicationContext)
                            break
                        }
                        i++
                    }
                }
            }
        console_ac.onFocusChangeListener = View.OnFocusChangeListener { view, b ->
            if (b) {
                console_ac.showDropDown()
            }
        }

        val style = Utils.ListOfString.listOfStyle()
        val styleAdapter = ArrayAdapter<String>(
            applicationContext,
            R.layout.simple_dropdown_item_1line,
            style
        )
        style_ac.setAdapter(styleAdapter)
        style_ac.threshold = 1
        style_ac.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                val selectedItem = parent.getItemAtPosition(position).toString()
                var j = 0
                if (style_chipgroup.childCount == 0) {
                    Toast.makeText(applicationContext, "Ajout $selectedItem", Toast.LENGTH_SHORT)
                        .show()
                    addChip(selectedItem, "Style",applicationContext)
                } else {
                    while (j < style_chipgroup.childCount) {
                        var chip: Chip = style_chipgroup.getChildAt(j) as Chip
                        if (chip.text == selectedItem) {
                            Toast.makeText(
                                applicationContext,
                                "Style $selectedItem déjà ajouté !",
                                Toast.LENGTH_SHORT
                            ).show()
                            clearAutocomplete(console_ac,style_ac)
                            break
                        }
                        if (j == style_chipgroup.childCount - 1) {
                            Toast.makeText(
                                applicationContext,
                                "Ajout $selectedItem",
                                Toast.LENGTH_SHORT
                            ).show()
                            addChip(selectedItem, "Style",applicationContext)
                            break
                        }
                        j++
                    }
                }
            }
        style_ac.onFocusChangeListener = View.OnFocusChangeListener { view, b ->
            if (b) {
                style_ac.showDropDown()
            }
        }
    }

    private fun clearAutocomplete(console_ac: AutoCompleteTextView, style_ac: AutoCompleteTextView) {
        console_ac.clearFocus()
        console_ac.text.clear()
        style_ac.clearFocus()
        style_ac.text.clear()
    }

    fun addChip(chipName: String, group: String, context: Context) {
        // Initialize a new chip instance
        val chip = Chip(context)
        chip.text = chipName
        chip.tag = group
        // Set the chip icon
        // chip.chipIcon = ContextCompat.getDrawable(this,R.drawable.ic_add_pic)
        when (chip.text) {
            "PS4", "PS3" -> {
                chip.setChipBackgroundColorResource(com.aceman.soireegaming.R.color.playstation)
            }
            "Xbox 360", "Xbox One" -> {
                chip.setChipBackgroundColorResource(com.aceman.soireegaming.R.color.xbox)
            }
            "PS2", "Xbox" -> {
                chip.setChipBackgroundColorResource(android.R.color.black)
            }
            "Dreamcast" -> {
                chip.setChipBackgroundColorResource(com.aceman.soireegaming.R.color.dreamcast)
            }
            "Gamecube" -> {
                chip.setChipBackgroundColorResource(com.aceman.soireegaming.R.color.gamecube)
            }
            "Wii", "Wii U", "3DS", "Switch" -> {
                chip.setChipBackgroundColorResource(com.aceman.soireegaming.R.color.wii)
            }
            "Android" -> {
                chip.setChipBackgroundColorResource(com.aceman.soireegaming.R.color.android)
            }
            "PC" -> {
                chip.setChipBackgroundColorResource(com.aceman.soireegaming.R.color.pc)
            }
            "Autre" -> {
                chip.setChipBackgroundColorResource(com.aceman.soireegaming.R.color.autre)
            }
        }
        chip.setTextColor(context.resources.getColor(com.aceman.soireegaming.R.color.primaryTextColor))

        chip.isClickable = true
        chip.isCheckable = false
        chip.isCloseIconVisible = true

      //  saveToFirestore(chip)
    }


    }
}