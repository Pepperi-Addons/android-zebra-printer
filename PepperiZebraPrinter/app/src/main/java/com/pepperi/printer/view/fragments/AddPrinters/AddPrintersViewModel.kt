package com.pepperi.printer.view.fragments.AddPrinters

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pepperi.printer.model.Repository
import com.pepperi.printer.model.api.ZebraApi
import com.pepperi.printer.model.entities.UserPrinter
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class AddPrintersViewModel(val repository: Repository,val  zebraApi: ZebraApi?) : ViewModel(){

    var discoveredPrintersLiveData : MutableLiveData<List<UserPrinter?>?> =  MutableLiveData<List<UserPrinter?>?>()

    init {
                scanPrinters()
        }

    fun scanPrinters(){
        viewModelScope.launch {
            collectPrinters()
        }
    }
   private suspend fun collectPrinters(){
       val list = arrayListOf<UserPrinter?>()
        zebraApi?.bluetoothScan()?.catch { exception -> Log.e("zebraScan_Error", exception.stackTraceToString()) } // catch and handle with errors
       ?.collect(){
            it.forEach {
                list.add(UserPrinter(
                    it?.discoveryDataMap?.getValue("FRIENDLY_NAME")?:"",
                    it?.discoveryDataMap?.getValue("MAC_ADDRESS")?:""
                    )
                )
            }
            discoveredPrintersLiveData.value = list
        }
    }

    }