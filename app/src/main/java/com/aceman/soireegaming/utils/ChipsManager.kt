package com.aceman.soireegaming.utils

import com.aceman.soireegaming.data.models.UserChip

/**
 * Created by Lionel JOFFRAY - on 25/11/2019.
 */

class ChipsManager {
    companion object{

         fun initListOfChip(chipList: MutableList<UserChip>) {
            val chipsConsole: Array<String> = Utils.ListOfString.listOfConsole()
            val chipStyle : Array<String> = Utils.ListOfString.listOfStyle()
            var i = 0
             for(item in chipsConsole){
                chipList.add(i, UserChip(chipsConsole[i],"Console",false))
                i++
            }
            for((j, item) in chipStyle.withIndex()){
                chipList.add(i, UserChip(chipStyle[j],"Style",false))
                i++
            }
        }
    }
}