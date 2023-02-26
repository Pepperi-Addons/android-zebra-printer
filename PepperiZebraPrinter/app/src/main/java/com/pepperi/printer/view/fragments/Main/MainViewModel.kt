package com.appa.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pepperi.printer.model.Repository
import com.pepperi.printer.model.entities.UserPrinterModel


class MainViewModel(val repository: Repository) : ViewModel(){

    var allPrintersLiveData : MutableLiveData<List<UserPrinterModel>?> =  MutableLiveData<List<UserPrinterModel>?>()

    init {
        getAllUserPrinters()
    }

    fun getAllUserPrinters(){
        allPrintersLiveData.value = repository.getAllUserPrinter()
    }
    fun removeUserPrinter(printerIndex: Int){
        repository.removePrinter(printerIndex)
    }

    fun setUserPrinterAsDefault( printerIndex: Int){

        val userPrinterModel = getUserPrinterByIndex(printerIndex)

        userPrinterModel?.let{ userPrinter ->
            resetDefaultUser()

            userPrinter.isDefault = true

            repository.replacePrinter(userPrinter,printerIndex)
        }

    }
    fun getUserPrinterByIndex(printerIndex: Int): UserPrinterModel? {
        return allPrintersLiveData.value?.get(printerIndex)
    }
    private fun resetDefaultUser(){
        allPrintersLiveData.value?.let { list ->
            for (i in 0 until list.size ){
                list[i].isDefault = false
            }
        }
    }
}


