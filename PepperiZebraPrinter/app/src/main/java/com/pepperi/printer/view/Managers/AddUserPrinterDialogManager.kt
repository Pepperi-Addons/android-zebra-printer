package com.pepperi.printer.view.Managers

import android.app.Dialog
import android.view.Window
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.pepperi.printer.R
import com.pepperi.printer.model.entities.UserPrinterModel
import com.pepperi.printer.view.fragments.AddPrinters.AddPrintersFragment
import com.pepperi.printer.view.fragments.AddPrinters.AddPrintersViewModel

class AddUserPrinterDialogManager(val fragment: AddPrintersFragment,val viewModel: AddPrintersViewModel) {

    val dialog = Dialog(fragment.requireContext())
    lateinit var friendly_name : TextInputEditText
    lateinit var add_printer_btn : Button

    fun showDialog(name: String, selectedPosition: Int){

        dialogSetting()

        initMembers()

        membersSetting(name, selectedPosition)

        dialog.show()

    }

    private fun initMembers() {

        friendly_name = dialog.findViewById(R.id.friendly_name_iet) as TextInputEditText

        add_printer_btn = dialog.findViewById(R.id.add_printer_btn) as Button
    }

    private fun membersSetting(name: String, selectedPosition: Int) {
        friendly_name.setText(name.toCharArray(),0,name.length)


        add_printer_btn.setOnClickListener {

            onClick(selectedPosition)
        }
    }

    private fun onClick(selectedPosition: Int) {

        viewModel.getPrinterByIndex(selectedPosition)?.let { selectedPrinter -> // get the selected printer by selectedPosition parameter
            val userPrinter = UserPrinterModel( // create user printer from discover printer
                friendly_name.text.toString(),
                selectedPrinter.name,
                selectedPrinter.mac)

            viewModel.saveUserPrinter(userPrinter)
        }
        fragment.findNavController().navigate(R.id.action_SecondFragment_to_MainFragment
        )
        dialog.dismiss()
    }

    private fun dialogSetting(){
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.add_printer_layout)
    }


}