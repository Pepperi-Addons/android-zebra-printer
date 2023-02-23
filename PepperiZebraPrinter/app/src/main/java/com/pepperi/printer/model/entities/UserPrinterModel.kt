package com.pepperi.printer.model.entities

data class UserPrinterModel(val friendly_name: String, val protocol_mode : String, val name: String, val mac: String ) {

    override fun toString(): String {
        return "UserPrinterModel(friendly_name='$friendly_name', protocol_mode='$protocol_mode', name='$name', mac='$mac')"
    }
}
