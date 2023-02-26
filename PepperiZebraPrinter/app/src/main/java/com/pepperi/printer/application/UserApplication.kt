package com.pepperi.printer.application

import android.app.Application
import com.pepperi.printer.model.Repository
import com.pepperi.printer.model.api.sharedPreferences.SharedPreferencesApi
import com.pepperi.printer.model.api.zebra.ZebraApi


/**
 * A application class where we can define the variable scope to use through out the application.
 */
class UserApplication() : Application(){

    val zebraApi by lazy { ZebraApi(applicationContext) }

    // A variable for repository.
    val sharedPreferencesApi by lazy { SharedPreferencesApi(applicationContext) }
    val repository by lazy { Repository(sharedPreferencesApi) } }

