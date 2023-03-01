package com.pepperi.printer.model

import android.net.MacAddress
import com.pepperi.printer.model.api.sharedPreferences.SharedPreferencesApi
import com.pepperi.printer.model.entities.UserPrinterModel


class Repository(private val sharedPreferencesApi: SharedPreferencesApi) {

    fun saveUserPrinter(userPrinterModel: UserPrinterModel){
        sharedPreferencesApi.saveUserPrinter(userPrinterModel)
    }

    fun getAllUserPrinters(): ArrayList<UserPrinterModel> {
       return sharedPreferencesApi.getAllUserPrinters()
    }
    fun removePrinter(macAddress: String){
        sharedPreferencesApi.removePrinter(macAddress)
    }
    fun removeAllPrinters(){
        sharedPreferencesApi.clearSharedPreferences()
    }
}