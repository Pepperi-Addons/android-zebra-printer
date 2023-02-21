package com.pepperi.printer.model.api

import android.content.Context
import android.util.Log
import com.zebra.sdk.printer.discovery.BluetoothDiscoverer
import com.zebra.sdk.printer.discovery.DiscoveredPrinter
import com.zebra.sdk.printer.discovery.DiscoveryException
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow

class ZebraApi
    (val context: Context) {

    suspend fun bluetoothScan() : Flow<ArrayList<DiscoveredPrinter?>> {
         val lastlist : Flow<ArrayList<DiscoveredPrinter?>> = channelFlow {
             val discoveryHandler = BluetoothDiscoveryHandler(this as ProducerScope<ArrayList<DiscoveredPrinter?>?>)
            try {
                Log.e("zebraScan","Starting printer discovery.")
                BluetoothDiscoverer.findPrinters(context,discoveryHandler)

            } catch (e: DiscoveryException) {
                Log.e("zebraScan_Error",e.stackTraceToString())
            }
             awaitClose()
        }
        return lastlist
    }
}