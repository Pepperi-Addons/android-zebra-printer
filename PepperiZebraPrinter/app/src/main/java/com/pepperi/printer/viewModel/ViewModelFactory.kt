package com.pepperi.printer.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.appa.viewModel.MainViewModel
import com.pepperi.printer.model.Repository
import com.pepperi.printer.model.api.zebra.ZebraApi
import com.pepperi.printer.view.fragments.AddPrinters.AddPrintersViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val repository: Repository, private val zebraApi: ZebraApi?  = null): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        when(modelClass){
            MainViewModel::class.java ->  MainViewModel(repository, zebraApi!!) as T
            AddPrintersViewModel::class.java ->  AddPrintersViewModel(repository, zebraApi!!) as T
            else -> throw Exception("Not Fund view model class")
        }



}