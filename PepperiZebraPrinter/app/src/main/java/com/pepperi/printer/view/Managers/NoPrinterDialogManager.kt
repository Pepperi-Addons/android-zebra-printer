package com.pepperi.printer.view.Managers

import android.app.Dialog
import android.view.Window
import android.widget.Button
import android.widget.TextView
import com.appa.viewModel.MainViewModel
import com.pepperi.printer.R
import com.pepperi.printer.view.fragments.Main.MainFragment

class NoPrinterDialogManager(val fragment: MainFragment, val viewModel: MainViewModel) {

    val dialog = Dialog(fragment.requireContext())
    lateinit var print_pdf_btn : Button
    lateinit var pdf_print : TextView

     fun showDialog(){

        dialogSetting()

        initMembers()

        membersSetting()

        dialog.show()

    }

    private fun initMembers() {


        print_pdf_btn = dialog.findViewById(R.id.print_pdf_btn) as Button

        pdf_print = dialog.findViewById(R.id.pdf_print) as TextView
    }

    private fun membersSetting() {

        print_pdf_btn.setOnClickListener {

            onClick()
        }

    }

    private fun onClick() {

        closeDialog()
    }


    private fun dialogSetting(){
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.pdf_print_no_user)
        dialog.setTitle(fragment.getString(R.string.no_printer_selected_title))
    }

    fun closeDialog(){
        fragment.activity?.finish()
        dialog.dismiss()
    }
}