package com.pepperi.printer.view.Managers

import android.app.Dialog
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.pepperi.printer.R
import com.pepperi.printer.model.PrintFormat
import com.pepperi.printer.model.entities.UserPrinterModel
import com.pepperi.printer.view.fragments.AddPrinters.AddPrintersFragment
import com.pepperi.printer.view.fragments.AddPrinters.AddPrintersViewModel

class AddUserPrinterDialogManager(val fragment: AddPrintersFragment,val viewModel: AddPrintersViewModel) {

    val dialog = Dialog(fragment.requireContext())
    val spinner = dialog.findViewById(R.id.protocol_mode_spnr) as Spinner
    val friendly_name = dialog.findViewById(R.id.friendly_name_iet) as TextInputEditText
    val add_printer_btn = dialog.findViewById(R.id.add_printer_btn) as Button

    fun showDialog(name: String, selectedPosition: Int){

        dialogSetting()

        membersSetting(name, selectedPosition)

        dialog.show()

    }

    private fun membersSetting(name: String, selectedPosition: Int) {
        friendly_name.setText(name.toCharArray(),0,name.length)

        spinner.adapter = setSpinnerAdapter()

        add_printer_btn.setOnClickListener {

            onClick(selectedPosition)
        }
    }

    private fun onClick(selectedPosition: Int) {

        viewModel.getPrinterByIndex(selectedPosition)?.let { selectedPrinter -> // get the selected printer by selectedPosition parameter
            val userPrinter = UserPrinterModel( // create user printer from discover printer
                friendly_name.text.toString(),
                spinner.selectedItem.toString(),
                selectedPrinter.name,
                selectedPrinter.mac)

            viewModel.saveUserPrinter(userPrinter)
        }
        fragment.findNavController().popBackStack()
        dialog.dismiss()
    }

    private fun dialogSetting(){
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.add_printer_layout)
    }

    private fun setSpinnerAdapter(): ArrayAdapter<String> {
        val spinnerArrayAdapter = ArrayAdapter<String>(
            fragment.requireContext(), android.R.layout.simple_spinner_item,
            arrayListOf(PrintFormat.PDF.format, PrintFormat.ZPL.format, PrintFormat.ESC_PSC.format)
        ) //selected item will look like a spinner set from XML

        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        return spinnerArrayAdapter
    }

}