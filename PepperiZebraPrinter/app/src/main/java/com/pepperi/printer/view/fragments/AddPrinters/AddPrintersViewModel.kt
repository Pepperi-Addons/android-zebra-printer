package com.pepperi.printer.view.fragments.AddPrinters

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.pepperi.printer.model.Repository
import com.pepperi.printer.model.entities.UserPrinter
import com.zebra.sdk.printer.discovery.DiscoveredPrinter
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AddPrintersViewModel(val repository : Repository) : ViewModel(){

    var discoveredPrintersLiveData : MutableLiveData<List<DiscoveredPrinter?>?> =  MutableLiveData<List<DiscoveredPrinter?>?>()

    init {
                scanPrinters()
        }

    fun scanPrinters(){
        viewModelScope.launch {
            collectPrinters()
        }
    }
   private suspend fun collectPrinters(){
        repository.scanPrinters().collect(){
            Log.e("getlist", "list: ${it.toString()} ")
            discoveredPrintersLiveData.value = it
        }
    }

    }