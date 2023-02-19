package com.pepperi.printer.application

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import com.pepperi.printer.model.Repository

/**
 * A application class where we can define the variable scope to use through out the application.
 */
class UserApplication() : Application() {

    // A variable for repository.
    val repository by lazy { Repository() }
}