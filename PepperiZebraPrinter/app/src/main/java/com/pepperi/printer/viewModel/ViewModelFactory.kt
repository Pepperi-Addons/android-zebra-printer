package com.pepperi.printer.viewModel

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.appa.viewModel.MainViewModel
import com.pepperi.printer.model.Repository
<<<<<<< Updated upstream

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val repository: Repository, private val connectivityManager : ConnectivityManager? = null): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        when(modelClass){
            MainViewModel::class.java ->  MainViewModel(repository) as T
=======
import com.pepperi.printer.model.api.zebra.ZebraApi
import com.pepperi.printer.view.fragments.AddPrinters.AddPrintersViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val repository: Repository, private val sharedPreferences: SharedPreferences? = null, private val zebraApi: ZebraApi?  = null): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        when(modelClass){
            MainViewModel::class.java ->  MainViewModel(repository, sharedPreferences!!) as T
            AddPrintersViewModel::class.java ->  AddPrintersViewModel(repository,sharedPreferences!!, zebraApi!!) as T
>>>>>>> Stashed changes
            else -> throw Exception("Not Fund view model class")
        }



}