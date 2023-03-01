package com.pepperi.printer.model.api.zebra

import android.content.Context
import android.net.Uri
import android.util.Base64
import android.util.Log
import com.pepperi.printer.model.entities.UserPrinterModel
import com.pepperi.printer.view.Managers.PrintDialogManager
import com.zebra.sdk.comm.BluetoothConnectionInsecure
import com.zebra.sdk.comm.Connection
import com.zebra.sdk.printer.ZebraPrinterFactory
import com.zebra.sdk.printer.discovery.BluetoothDiscoverer
import com.zebra.sdk.printer.discovery.DiscoveredPrinter
import com.zebra.sdk.printer.discovery.DiscoveryException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class ZebraApi
    (val context: Context) {

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

     fun printData(data: Uri, defaultPrinter: UserPrinterModel, dialogManager: PrintDialogManager) {


            val parameters = data.queryParameterNames.associateWith { data.getQueryParameters(it) }

            val dataURI = parameters["dataURI"]?.get(0).toString()

            val dataToPrint = getDataToPrint(dataURI)

         val thePrinterConn: Connection = BluetoothConnectionInsecure(
             defaultPrinter.mac )

            if (dataURI.contains("x-application/zpl")
                || dataURI.contains("application/vnd.hp-PCL")){

                GlobalScope.launch(Dispatchers.IO){

                    printZPL(thePrinterConn,dataToPrint)

                    closePrint(thePrinterConn,dialogManager)
                }

            }else if(dataURI.contains("application/pdf")) {
                GlobalScope.launch(Dispatchers.IO){

                    printPDF(thePrinterConn,getFile(dataToPrint))

                    closePrint(thePrinterConn,dialogManager)
                }
                Log.e("application/pdf","End")
            } else {

                closePrint(thePrinterConn,dialogManager)
            }
//        }?: Log.e("Print error", "No printer selected")
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

    private suspend fun printZPL(thePrinterConn: Connection, data : String) {
        try {

            coroutineScope {
                // Open the connection - physical connection is established here.
                thePrinterConn.open()
                // This example prints "This is a ZPL test." near the top of the label.

                // This example prints "This is a ZPL test." near the top of the label.
                val zplData = data

                // Send the data to printer as a byte array.

                // Send the data to printer as a byte array.
                thePrinterConn.write(zplData.toByteArray())
            }
        }catch (e : Exception){
            Log.e("printException", e.stackTraceToString())
        }
        Log.e("printZPL","End")
    }

    private suspend fun printPDF(thePrinterConn: Connection, pdfFile: File) {
        try {
            coroutineScope() {
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
                }
            }
        }catch (e : Exception){
            Log.e("printException", e.stackTraceToString())
        }
        Log.e("printPDF","End")
    }

    private fun closePrint(thePrinterConn: Connection, dialogManager: PrintDialogManager ){
        thePrinterConn.close()
        dialogManager.closeDialog()
    }
}