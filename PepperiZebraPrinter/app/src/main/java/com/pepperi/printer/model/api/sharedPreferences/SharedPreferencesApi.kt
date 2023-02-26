package com.pepperi.printer.model.api.sharedPreferences

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.pepperi.printer.model.entities.UserPrinterModel

class SharedPreferencesApi(private val context: Context) {

    private val sharedPreferences = context.getSharedPreferences("USER_SP",Context.MODE_PRIVATE)

    //  THe function add user to the sharedPreferences
    fun saveUserPrinter(userPrinterModel: UserPrinterModel){
        val userPrinterString = printerToStringData(userPrinterModel)

        saveUserPrinterString(userPrinterString)
    }

    private fun saveUserPrinterString( userPrinterString: String){
        val printersDataString = getStringPrintersData()

        var newData = ""

        if (printersDataString != ""){
            newData = "${printersDataString}**${userPrinterString}" // Adding the sign ** to mark the start of a new user printer
        }else {
            newData = userPrinterString
        }
        with (sharedPreferences.edit()) {
            putString("USER_PRINTERS", newData)
            Log.e("sharedPreferences", "save: ${newData}")
            commit()
        }
    }
    // THe function load all the saved user printers from sharedPreferences to list
    fun LoadAllUserPrinters() : ArrayList<UserPrinterModel>{
        val returnedListOfUserPrinter = arrayListOf<UserPrinterModel>()

        val gson = Gson()

        val printersDataString = getStringPrintersData()

        if (printersDataString != "") {
            val printersDataStringArray =
                printersDataString.split("**") //  split the data by  ** to create string array of user printers

            for (i in 0 until printersDataStringArray.size) {
                val userPrinter = gson.fromJson(
                    printersDataStringArray[i],
                    UserPrinterModel::class.java
                ) // get UserPrinterModel object from string data by using GSON

                Log.e("sharedPreferences", "get userPrinter: ${userPrinter.toString()}")
                returnedListOfUserPrinter.add(userPrinter)
            }
        }
        return returnedListOfUserPrinter
    }
    // get data from sharedPreferences (arrived by string format)
    private fun getStringPrintersData() : String{
        return sharedPreferences.getString("USER_PRINTERS", "") ?: ""
    }
    // The function return string data from UserPrinterModel object by using GSON
    private fun printerToStringData(userPrinterModel: UserPrinterModel) :String{
        val gson = Gson()
        val userPrinterString: String = gson.toJson(userPrinterModel)
        Log.e("Gson", userPrinterString)
        return userPrinterString
    }
    fun removePrinter(printerIndex: Int){
        val printersDataString = getStringPrintersData()

        if (printersDataString != ""){

            val printersDataStringArray =
                printersDataString.split("**") //  split the data by  ** to create string array of user printers

            clearSharedPreferences()

            for (i in 0 until printersDataStringArray.size){
                if (i != printerIndex){
                    saveUserPrinterString(printersDataStringArray[i])
                }
            }
        }
    }
    fun replacePrinter( substitutePrinterModel: UserPrinterModel, printerIndex: Int) {
        val printersDataString = getStringPrintersData()

        if (printersDataString != "") {

            val printersDataStringArray =
                printersDataString.split("**") //  split the data by  ** to create string array of user printers

            clearSharedPreferences()

            for (i in 0 until printersDataStringArray.size) {
                if (i != printerIndex) {
                    saveUserPrinterString(printersDataStringArray[i])
                }else{
                    saveUserPrinter(substitutePrinterModel)
                }
            }
        }
    }
    private fun clearSharedPreferences(){
        with (sharedPreferences.edit()) {
            clear()
            commit()
        }
    }
}