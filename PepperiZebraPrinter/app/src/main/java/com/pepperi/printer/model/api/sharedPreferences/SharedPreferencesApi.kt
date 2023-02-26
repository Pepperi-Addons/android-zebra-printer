package com.pepperi.printer.model.api.sharedPreferences

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pepperi.printer.model.entities.UserPrinterModel


class SharedPreferencesApi(private val context: Context) {

    private val sharedPreferences = context.getSharedPreferences("USER_SP",Context.MODE_PRIVATE)

    //  THe function add user to the sharedPreferences
    fun saveUserPrinter(userPrinterModel: UserPrinterModel){
        val printersDataList = getPrintersData()

        var newData = ""

        printersDataList.add(userPrinterModel)

        newData = ListToStringData(printersDataList)

        with (sharedPreferences.edit()) {
            putString("USER_PRINTERS", newData)
            apply()
        }
    }
    // THe function load all the saved user printers from sharedPreferences to list
    fun getAllUserPrinters() : ArrayList<UserPrinterModel>{
        return getPrintersData()
    }
    // get data from sharedPreferences (arrived by string format)
    private fun getPrintersData() : ArrayList<UserPrinterModel>{
        var returnedList = arrayListOf<UserPrinterModel>()

        val stringData = sharedPreferences.getString("USER_PRINTERS", "") ?: ""

        val myType = object : TypeToken<List<UserPrinterModel>>() {}.type
        if (stringData != ""){
            returnedList = Gson().fromJson(stringData, myType);
        }
        Log.e("getPrintersData", returnedList.toString())
        return returnedList
    }
    // The function return string data from UserPrinterModel object by using GSON
    private fun ListToStringData(listUserPrinter: ArrayList<UserPrinterModel>) :String{
        val gson = Gson()
        val userPrinterString: String = gson.toJson(listUserPrinter)
        Log.e("ListToStringData", userPrinterString)
        return userPrinterString
    }

}