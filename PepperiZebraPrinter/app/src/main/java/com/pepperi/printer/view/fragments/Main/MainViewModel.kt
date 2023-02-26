package com.appa.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pepperi.printer.model.PrintFormat
import com.pepperi.printer.model.Repository
import com.pepperi.printer.model.entities.UserPrinterModel
import com.zebra.sdk.comm.BluetoothConnectionInsecure
import com.zebra.sdk.comm.Connection
import kotlinx.coroutines.coroutineScope


class MainViewModel(val repository: Repository) : ViewModel(){

    var allPrintersLiveData : MutableLiveData<List<UserPrinterModel>?> =  MutableLiveData<List<UserPrinterModel>?>()

    init {
        getAllUserPrinters()
    }

    fun getAllUserPrinters(){
        allPrintersLiveData.value = repository.getAllUserPrinters()
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

      suspend fun print(selectedPrinter: Int) {
        try {
            val thePrinterConn: Connection = BluetoothConnectionInsecure(
                allPrintersLiveData.value?.get(selectedPrinter)?.mac
                ?:"" )
            coroutineScope() {
                // Open the connection - physical connection is established here.
                // Open the connection - physical connection is established here.
                thePrinterConn.open()
                Log.e("thePrinterConn", "start")
                // This example prints "This is a ZPL test." near the top of the label.

                // This example prints "This is a ZPL test." near the top of the label.
                val zplData = "^XA^FO20,20^A0N,25,25^FDThis is a ZPL test.^FS^XZ"

                // Send the data to printer as a byte array.

                // Send the data to printer as a byte array.
                thePrinterConn.write(zplData.toByteArray())
            }
            Log.e("thePrinterConn", "close")
            thePrinterConn.close()
        }catch (e : Exception){
            Log.e("printException", e.stackTraceToString())
        }
    }
}


