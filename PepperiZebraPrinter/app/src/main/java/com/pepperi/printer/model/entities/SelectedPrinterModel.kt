package com.pepperi.printer.model.entities

data class SelectedPrinterModel(val name: String, val mac: String ,var isSelected: Boolean = false) {
}