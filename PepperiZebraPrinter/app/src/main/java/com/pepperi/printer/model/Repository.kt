package com.pepperi.printer.model

import com.pepperi.printer.model.entities.UserPrinter


class Repository() {


    fun allPrinter() = hardCodedList()

    private fun hardCodedList(): List<UserPrinter> {

        val newList = arrayListOf <UserPrinter>()

//        newList.add(UserPrinter(0,"test1"))
//        newList.add(UserPrinter(1,"test2"))

        return newList
    }

}