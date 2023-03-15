package com.pepperi.printer.model.api.zebra

import android.content.Context
import android.util.Base64
import android.util.Log
import com.zebra.sdk.comm.BluetoothConnectionInsecure
import com.zebra.sdk.comm.Connection
import com.zebra.sdk.printer.ZebraPrinterFactory
import com.zebra.sdk.printer.discovery.BluetoothDiscoverer
import com.zebra.sdk.printer.discovery.DiscoveredPrinter
import com.zebra.sdk.printer.discovery.DiscoveryException
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


public interface OnFinishPrintListener{
    fun onFinishPrintListener(isPrintSuccess : Boolean)
}

class ZebraApi
    (val context: Context) {

    var isPrintSuccess = false
    var finishPrintListener: OnFinishPrintListener? = null

    //The function Scan of Zebra bluetooth printers and return them as List of DiscoveredPrinter
    suspend fun bluetoothScan() : Flow<ArrayList<DiscoveredPrinter?>> {
         val lastlist : Flow<ArrayList<DiscoveredPrinter?>> = channelFlow {
             val discoveryHandler = BluetoothDiscoveryHandler(this as ProducerScope<ArrayList<DiscoveredPrinter?>?>)

            try {
                Log.e("zebraScan","Starting printer discovery.")
                BluetoothDiscoverer.findPrinters(context,discoveryHandler)

            } catch (e: DiscoveryException) {
                Log.e("zebraScan_Error",e.stackTraceToString())
            }

             awaitClose()   // function was forces the channelFlow to wait for results
        }
        return lastlist
    }

     fun printData(dataURI: String, macAddress: String) {

         val dataToPrint = getDataToPrint(dataURI)

            var printerConn: Connection? = null
         try {
             printerConn = BluetoothConnectionInsecure(
                 macAddress)

         }catch (e : Exception){
             Log.e("printException", e.stackTraceToString())
         }

         printerConn?.let { thePrinterConn ->
             if (dataURI.contains("x-application/zpl")
                 || dataURI.contains("application/vnd.hp-PCL")
             ) {

                 GlobalScope.launch(Dispatchers.IO) {

                     printZPL(thePrinterConn, dataToPrint)

                     async {
                         withContext(Dispatchers.Main) {
                             checkIsPrintSuccess(thePrinterConn)
                         }
                     }

                 }

             } else if (dataURI.contains("application/pdf")) {
                 GlobalScope.launch(Dispatchers.IO) {

                     printPDF(thePrinterConn, getFile(dataToPrint))

                     async {
                         withContext(Dispatchers.Main) {
                             checkIsPrintSuccess(thePrinterConn)
                         }
                     }
                 }
             } else {

                 checkIsPrintSuccess(thePrinterConn)
             }
         }
    }

    private fun checkIsPrintSuccess(thePrinterConn: Connection) {

        thePrinterConn.close()

        finishPrintListener?.onFinishPrintListener(isPrintSuccess)
    }

    private fun getDataToPrint(dataURI: String): String {

        val dataArryeHelper = dataURI.split(",")

        return dataArryeHelper[1]
    }

    private fun getFile(dataPrint:String) : File {
        val path = context.filesDir
        val file = File(path, "test_pdf.pdf")
        try {
            Log.e("getFile",file.path)
            val outputStreamWriter = FileOutputStream(file)
            val decodedString = Base64.decode(dataPrint, Base64.DEFAULT)
            outputStreamWriter.write(decodedString)
            outputStreamWriter.close()
        } catch (e: IOException) {
            Log.e("Exception", "File write failed: $e")
        }
        return file

    }

    private suspend fun printZPL(
        thePrinterConn: Connection,
        data: String
    ) {
        coroutineScope {
        try {
                // Open the connection - physical connection is established here.
                thePrinterConn.open()

                val zplData = data

                // Send the data to printer as a byte array.
                thePrinterConn.write(zplData.toByteArray())

            isPrintSuccess = true

        }catch (e : Exception){
            Log.e("printException", e.stackTraceToString())
            isPrintSuccess =false
        }
        }
        Log.e("printZPL","End")
    }

    private suspend fun printPDF(
        thePrinterConn: Connection,
        pdfFile: File
    ) {
        coroutineScope() {
        try {

                // Open the connection - physical connection is established here.
                if (!thePrinterConn.isConnected()) {
                    thePrinterConn.open();
                }

                // Get Instance of Printer
                val printer = ZebraPrinterFactory.getLinkOsPrinter(thePrinterConn);

                // Verify Printer Status is Ready
                val printerStatus = printer.currentStatus;
                if (printerStatus.isReadyToPrint) {
                    // Send the data to printer as a byte array.
                    printer.sendFileContents(pdfFile.getAbsolutePath())
                } else {

                }
            isPrintSuccess =true

        }catch (e : Exception) {
            Log.e("printException", e.stackTraceToString())
            isPrintSuccess = false
        }
        }
        Log.e("printPDF","End")
    }

    fun setOnFinishPrintListener(setOnFinishPrintListener: OnFinishPrintListener){
        finishPrintListener = setOnFinishPrintListener
    }
}