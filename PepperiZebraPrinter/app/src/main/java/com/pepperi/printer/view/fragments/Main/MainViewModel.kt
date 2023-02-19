package com.appa.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pepperi.printer.model.Repository
import com.pepperi.printer.model.entities.UserPrinter

import kotlinx.coroutines.launch


class MainViewModel(val repository :Repository) : ViewModel(){

    var allPrintersLiveData : MutableLiveData<List<UserPrinter>?> =  MutableLiveData<List<UserPrinter>?>()

    init {
        repository.allPrinter().let {
            allPrintersLiveData.value = it
        }
    }
}


