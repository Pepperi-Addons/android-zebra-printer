package com.pepperi.printer.model

import android.content.SharedPreferences
import com.pepperi.printer.model.api.sharedPreferences.SharedPreferencesApi
import com.pepperi.printer.model.entities.UserPrinterModel


class Repository(private val sharedPreferencesApi: SharedPreferencesApi) {

    fun saveUserPrinter(userPrinterModel: UserPrinterModel, sharedPreferences : SharedPreferences){
        sharedPreferencesApi.saveUserPrinter(userPrinterModel, sharedPreferences)
    }

    fun getAllUserPrinter(sharedPreferences: SharedPreferences): ArrayList<UserPrinterModel> {
       return sharedPreferencesApi.LoadAllUserPrinters(sharedPreferences)
    }


}