package com.appa.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pepperi.printer.model.Repository
import com.pepperi.printer.model.entities.UserPrinterModel
import com.zebra.sdk.comm.BluetoothConnectionInsecure
import com.zebra.sdk.comm.Connection
import com.zebra.sdk.printer.ZebraPrinterFactory
import kotlinx.coroutines.coroutineScope
import java.io.File


class MainViewModel(val repository: Repository) : ViewModel(){

    var allPrintersLiveData : MutableLiveData<List<UserPrinterModel>?> =  MutableLiveData<List<UserPrinterModel>?>()
    var userDefaultPrinter : Int? =  null

    init {
        getAllUserPrinters()
        userDefaultPrinter =  getDefaultPrinter()
    }

    fun getAllUserPrinters(){
        allPrintersLiveData.value = repository.getAllUserPrinters()
    }
    fun removeUserPrinter(printerIndex: Int){
        repository.removePrinter(allPrintersLiveData.value?.get(printerIndex)?.mac ?:"")
    }

    fun setUserPrinterAsDefault( printerIndex: Int){

        val userPrinterModel = getUserPrinterByIndex(printerIndex)

        userPrinterModel?.let{ userPrinter ->
            resetDefaultUser()

            userPrinter.isDefault = true

            repository.replacePrinter(userPrinter, allPrintersLiveData.value?.get(printerIndex)?.mac ?:"")
        }

    }
    fun getUserPrinterByIndex(printerIndex: Int): UserPrinterModel? {
        return allPrintersLiveData.value?.get(printerIndex)
    }
    private fun resetDefaultUser(){

        removeAllPrinters()

        allPrintersLiveData.value?.let { list ->
            for (i in 0 until list.size ){
                list[i].isDefault = false
                repository.saveUserPrinter(list[i])
            }
        }

        getAllUserPrinters()
    }

    private fun getDefaultPrinter(): Int? {

        var returnedIndex : Int? = null

        allPrintersLiveData.value?.let { list ->
            for (i in 0 until list.size ){
                if (list[i].isDefault == true){
                    returnedIndex = i
                }
            }
        }
        return returnedIndex
    }

    private fun removeAllPrinters(){
        repository.removeAllPrinters()
    }
      suspend fun printZPL(macAddress: String, data : String) {
        try {
            val thePrinterConn: Connection = BluetoothConnectionInsecure(
                macAddress )
            coroutineScope {
                // Open the connection - physical connection is established here.
                thePrinterConn.open()
                // This example prints "This is a ZPL test." near the top of the label.

                // This example prints "This is a ZPL test." near the top of the label.
                val zplData = "data.toByteArray().decodeToString()"

                // Send the data to printer as a byte array.

                // Send the data to printer as a byte array.
                thePrinterConn.write(zplData.toByteArray())
            }
        }catch (e : Exception){
            Log.e("printException", e.stackTraceToString())
        }
    }

    suspend fun print(macAddress: String,  pdfFile: File) {
        try {
            val thePrinterConn: Connection = BluetoothConnectionInsecure(
                macAddress )
            coroutineScope() {
                // Open the connection - physical connection is established here.
                if (!thePrinterConn.isConnected()) {
                    thePrinterConn.open();
                }

                // Get Instance of Printer
                val printer = ZebraPrinterFactory.getLinkOsPrinter(thePrinterConn);

                // Verify Printer Status is Ready
                val printerStatus = printer.getCurrentStatus();
                if (printerStatus.isReadyToPrint) {
                    // Send the data to printer as a byte array.
                    printer.sendFileContents(pdfFile.getAbsolutePath())
                }
            }
        }catch (e : Exception){
            Log.e("printException", e.stackTraceToString())
        }
    }
}


