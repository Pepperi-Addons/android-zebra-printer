package com.pepperi.printer.model

import com.pepperi.printer.model.api.sharedPreferences.SharedPreferencesApi
import com.pepperi.printer.model.entities.UserPrinterModel


class Repository(private val sharedPreferencesApi: SharedPreferencesApi) {

    fun saveUserPrinter(userPrinterModel: UserPrinterModel){
        sharedPreferencesApi.saveUserPrinter(userPrinterModel)
    }

    fun getAllUserPrinters(): ArrayList<UserPrinterModel> {
       return sharedPreferencesApi.getAllUserPrinters()
    }
    fun removePrinter(printerIndex :Int){
        sharedPreferencesApi.removePrinter(printerIndex)
    }
    fun replacePrinter(replacementPrinterModel: UserPrinterModel, printerIndex: Int){
        sharedPreferencesApi.replacePrinter(replacementPrinterModel, printerIndex)
    }
}