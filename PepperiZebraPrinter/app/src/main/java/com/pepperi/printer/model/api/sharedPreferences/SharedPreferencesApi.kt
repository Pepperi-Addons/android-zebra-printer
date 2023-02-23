package com.pepperi.printer.model.api.sharedPreferences

import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.pepperi.printer.model.entities.UserPrinterModel

class SharedPreferencesApi {

    //  THe function add user to the sharedPreferences
    fun saveUserPrinter(userPrinterModel: UserPrinterModel, sharedPreferences : SharedPreferences){
        val printersDataString = getStringPrintersData(sharedPreferences) //
        var newData = ""
        if (printersDataString != ""){
            newData = "${printersDataString}**${printerToStringData(userPrinterModel)}" // Adding the sign ** to mark the start of a new user printer
        }else {
            newData = printerToStringData(userPrinterModel)
        }
        with (sharedPreferences.edit()) {
            putString("USER_PRINTERS", newData)
            Log.e("sharedPreferences", "save: ${newData}")
            apply()
        }
    }
    // THe function load all the saved user printers from sharedPreferences to list
    fun LoadAllUserPrinters(sharedPreferences : SharedPreferences) : ArrayList<UserPrinterModel>{
        val returnedListOfUserPrinter = arrayListOf<UserPrinterModel>()

        val gson = Gson()

        val printersDataString = getStringPrintersData(sharedPreferences)

        val printersDataStringArray = printersDataString.split("**") //  split the data by  ** to create string array of user printers

        for (i in 0 until printersDataStringArray.size){
            val userPrinter = gson.fromJson(printersDataStringArray[i],UserPrinterModel::class.java) // get UserPrinterModel object from string data by using GSON

            Log.e("sharedPreferences", "get userPrinter: ${userPrinter.toString()}")
            returnedListOfUserPrinter.add(userPrinter)
        }
        return returnedListOfUserPrinter
    }
    // get data from sharedPreferences (arrived by string format)
    private fun getStringPrintersData(sharedPreferences : SharedPreferences) : String{
        return sharedPreferences.getString("USER_PRINTERS", "") ?: ""
    }
    // The function return string data from UserPrinterModel object by using GSON
    private fun printerToStringData(userPrinterModel: UserPrinterModel) :String{
        val gson = Gson()
        val userPrinterString: String = gson.toJson(userPrinterModel)
        Log.e("Gson", userPrinterString)
        return userPrinterString
    }

}