package com.pepperi.printer.model.entities

data class UserPrinterModel(val friendly_name: String, val name: String, val mac: String, var isDefault :Boolean = false ) {

    override fun toString(): String {
        return "UserPrinterModel(friendly_name='$friendly_name', name='$name', mac='$mac', isDefault='$isDefault')"
    }
}
