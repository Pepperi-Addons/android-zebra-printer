package com.pepperi.printer.viewModel

import android.net.ConnectivityManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.appa.viewModel.MainViewModel
import com.pepperi.printer.model.Repository
import com.pepperi.printer.view.fragments.AddPrinters.AddPrintersViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val repository: Repository, private val connectivityManager : ConnectivityManager? = null): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        when(modelClass){
            MainViewModel::class.java ->  MainViewModel(repository) as T
            AddPrintersViewModel::class.java ->  AddPrintersViewModel(repository) as T
            else -> throw Exception("Not Fund view model class")
        }



}