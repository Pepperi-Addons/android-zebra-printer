package com.pepperi.printer.model.api

import android.util.Log
import com.zebra.sdk.printer.discovery.DiscoveredPrinter
import com.zebra.sdk.printer.discovery.DiscoveryHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.launch


class BluetoothDiscoveryHandler(val flowCollector: ProducerScope<ArrayList<DiscoveredPrinter?>?>) : DiscoveryHandler {

     val discoveryComplete
        get() = thisDiscoveryComplete
    private var thisDiscoveryComplete = false
    var printers: ArrayList<DiscoveredPrinter?> = arrayListOf()
    override fun foundPrinter(discoveredPrinter: DiscoveredPrinter?) {
        printers.add(discoveredPrinter)
        Log.e("DiscoveryHandler", "discover fund ${discoveredPrinter?.discoveryDataMap?.getValue("FRIENDLY_NAME")} printer, MAC:  ${discoveredPrinter?.discoveryDataMap?.getValue("MAC_ADDRESS")}" )
        Log.e("DiscoveryHandler", "discover fund ${printers.size} printers" )
    }

    override fun discoveryFinished() {
        Log.e("DiscoveryHandler", "discoveryFinished fund ${printers.size} printers" )
        GlobalScope.launch(Dispatchers.IO) {flowCollector.send(printers)  }
        thisDiscoveryComplete = true
    }

    override fun discoveryError(error: String?) {
        Log.e("DiscoveryHandler", error.toString() )
        GlobalScope.launch(Dispatchers.IO) {flowCollector.send(null)  }
        thisDiscoveryComplete = true
    }


}