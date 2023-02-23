package com.pepperi.printer.view.fragments.AddPrinters

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pepperi.printer.model.Repository
import com.pepperi.printer.model.api.zebra.ZebraApi
import com.pepperi.printer.model.entities.SelectedPrinterModel
import com.pepperi.printer.model.entities.UserPrinterModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class AddPrintersViewModel(val repository: Repository,val sharedPreferences: SharedPreferences, val  zebraApi: ZebraApi) : ViewModel(){

    var discoveredPrintersLiveData : MutableLiveData<List<SelectedPrinterModel?>?> =  MutableLiveData<List<SelectedPrinterModel?>?>()

    init {
                scanPrinters()
        }
    fun clearPrinters(){
        val emptyList = listOf<SelectedPrinterModel?>()
        discoveredPrintersLiveData.value = emptyList
    }

    fun scanPrinters(){
        viewModelScope.launch {
            collectPrinters()
        }
    }
   private suspend fun collectPrinters(){
       val list = arrayListOf<SelectedPrinterModel?>()
        zebraApi.bluetoothScan().catch { exception -> Log.e("zebraScan_Error", exception.stackTraceToString()) } // catch and handle with errors
       ?.collect(){
            it.forEach {
                list.add(SelectedPrinterModel(
                    it?.discoveryDataMap?.getValue("FRIENDLY_NAME")?:"",
                    it?.discoveryDataMap?.getValue("MAC_ADDRESS")?:""
                    )
                )
            }
            discoveredPrintersLiveData.value = list
        }
    }

    fun getList() : List<SelectedPrinterModel?>? {
        return discoveredPrintersLiveData.value
    }

    fun getPrinterByIndex(index: Int) : SelectedPrinterModel?{

        val list = getList()
        var returnPrinter: SelectedPrinterModel? = null // member to save the returned, printer return null if index is out of bounds

        list?.let { notNullLIst ->
            if (notNullLIst.size > index ){
                returnPrinter = notNullLIst[index]
            }
        }
        return returnPrinter
    }

    fun saveUserPrinter(userPrinterModel: UserPrinterModel){
        repository.saveUserPrinter(userPrinterModel, sharedPreferences)
    }


    }