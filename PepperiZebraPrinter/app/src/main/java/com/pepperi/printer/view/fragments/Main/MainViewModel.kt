package com.appa.viewModel

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pepperi.printer.model.Repository
import com.pepperi.printer.model.entities.UserPrinterModel


class MainViewModel(val repository: Repository,val sharedPreferences: SharedPreferences) : ViewModel(){

    var allPrintersLiveData : MutableLiveData<List<UserPrinterModel>?> =  MutableLiveData<List<UserPrinterModel>?>()

    init {
        getAllUserPrinters()
    }

    fun getAllUserPrinters(){
        allPrintersLiveData.value = repository.getAllUserPrinter(sharedPreferences)
    }
}


