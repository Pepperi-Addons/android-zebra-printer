package com.pepperi.printer.model.api.sharedPreferences

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pepperi.printer.model.entities.UserPrinterModel


class SharedPreferencesApi(private val context: Context) {

    private val sharedPreferences = context.getSharedPreferences("USER_SP",Context.MODE_PRIVATE)

    //  THe function add user to the sharedPreferences
    fun saveUserPrinter(userPrinterModel: UserPrinterModel){

        val printersDataList = getPrintersData()

        var newData = ""

        if(!isPrinterExist(printersDataList, userPrinterModel.mac)){
            printersDataList.add(userPrinterModel)
        }else {
            replacePrinter(userPrinterModel,userPrinterModel.mac)
        }

        newData = ListToStringData(printersDataList)

        with (sharedPreferences.edit()) {
            putString("USER_PRINTERS", newData)
            apply()
        }
    }

    private fun isPrinterExist(printersDataList: ArrayList<UserPrinterModel>, mac: String): Boolean {
        var returnAnswer = false

        for (i in 0 until printersDataList.size){
            if (printersDataList[i].mac == mac){
                returnAnswer = true
            }
        }
        return returnAnswer
    }

    // THe function load all the saved user printers from sharedPreferences to list
    fun getAllUserPrinters() : ArrayList<UserPrinterModel>{
        return getPrintersData()

    }
    // get data from sharedPreferences 
    private fun getPrintersData() : ArrayList<UserPrinterModel>{
        var returnedList = arrayListOf<UserPrinterModel>()

        val stringData = sharedPreferences.getString("USER_PRINTERS", "") ?: ""

        val myType = object : TypeToken<List<UserPrinterModel>>() {}.type
        if (stringData != ""){
            returnedList = Gson().fromJson(stringData, myType);
        }
        return returnedList
    }
    // The function return string data from UserPrinterModel object by using GSON
    private fun ListToStringData(listUserPrinter: ArrayList<UserPrinterModel>) :String{
        val gson = Gson()
        val userPrinterString: String = gson.toJson(listUserPrinter)
        return userPrinterString
    }

    fun removePrinter(mac: String){
        val printersData = getPrintersData()

        if (printersData.isNotEmpty()){

            for (i in 0 until printersData.size){
                if (printersData[i].mac == mac){
                    printersData.removeAt(i)
                }
            }

            clearSharedPreferences()

            saveListOfPrinter(printersData)
        }
    }
    // used only if sharedPreferences is empty
    private fun saveListOfPrinter(printersData: java.util.ArrayList<UserPrinterModel>) {

       val newData = ListToStringData(printersData)

        with (sharedPreferences.edit()) {
            putString("USER_PRINTERS", newData)
            apply()
        }
    }

    fun replacePrinter(replacementPrinterModel: UserPrinterModel, mac: String) {
        val printersData = getPrintersData()

        if (printersData.isNotEmpty()){

            for (i in 0 until printersData.size){
                if (printersData[i].mac == mac){
                    printersData[i] = replacementPrinterModel
                }
            }

            clearSharedPreferences()

            saveListOfPrinter(printersData)
        }
    }
     fun clearSharedPreferences(){
        with (sharedPreferences.edit()) {
            clear()
            commit()
        }
    }
}