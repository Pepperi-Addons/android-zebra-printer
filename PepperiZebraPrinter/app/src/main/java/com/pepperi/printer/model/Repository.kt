package com.pepperi.printer.model

import com.pepperi.printer.model.api.ZebraApi
import com.pepperi.printer.model.entities.UserPrinter
import com.zebra.sdk.printer.discovery.DiscoveredPrinter
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class Repository(val zebraApi: ZebraApi) {


    fun allPrinter() = hardCodedList()

    suspend fun scanPrinters() =
        zebraApi.bluetoothScan()


    private fun hardCodedList(): List<UserPrinter> {

        val newList = arrayListOf <UserPrinter>()

//        newList.add(UserPrinter(0,"test1"))
//        newList.add(UserPrinter(1,"test2"))

        return newList
    }

}