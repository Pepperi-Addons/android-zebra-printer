package com.pepperi.printer.model

import com.pepperi.printer.model.api.sharedPreferences.SharedPreferencesApi
import com.pepperi.printer.model.entities.UserPrinterModel


class Repository(private val sharedPreferencesApi: SharedPreferencesApi) {

    fun saveUserPrinter(userPrinterModel: UserPrinterModel){
        sharedPreferencesApi.saveUserPrinter(userPrinterModel)
    }

    fun getAllUserPrinter(): ArrayList<UserPrinterModel> {
       return sharedPreferencesApi.LoadAllUserPrinters()
    }
    fun removePrinter(printerIndex :Int){
        sharedPreferencesApi.removePrinter(printerIndex)
    }
    fun replacePrinter(substitutePrinterModel: UserPrinterModel, printerIndex: Int){
        sharedPreferencesApi.replacePrinter(substitutePrinterModel, printerIndex)
    }
}