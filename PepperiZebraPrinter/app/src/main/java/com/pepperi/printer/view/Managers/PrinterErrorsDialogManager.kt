package com.pepperi.printer.view.Managers

import android.app.Dialog
import android.util.DisplayMetrics
import android.view.Window
import android.widget.Button
import android.widget.TextView
import com.appa.viewModel.MainViewModel
import com.pepperi.printer.R
import com.pepperi.printer.view.fragments.Main.MainFragment


class PrinterErrorsDialogManager(val fragment: MainFragment, val viewModel: MainViewModel) {

    val dialog = Dialog(fragment.requireContext())
    lateinit var print_pdf_btn : Button
    lateinit var pdf_print : TextView

     fun showDialog(text: String){

        dialogSetting()

        initMembers()

        membersSetting(text)


        dialog.show()

    }

    private fun initMembers() {


        print_pdf_btn = dialog.findViewById(R.id.print_pdf_btn) as Button

        pdf_print = dialog.findViewById(R.id.pdf_print) as TextView
    }

    private fun membersSetting(text: String) {

        print_pdf_btn.setOnClickListener {

            onClick()
        }

        pdf_print.text = text
    }

    private fun onClick() {

        closeDialog()
    }


    private fun dialogSetting(){

        dialog.setCancelable(false)
        dialog.setContentView(R.layout.error_printers)
        dialog.setTitle(fragment.getString(R.string.error_printers_title))

        val metrics: DisplayMetrics = fragment.getResources().getDisplayMetrics()
        val width = (fragment.getResources().getDisplayMetrics().widthPixels*0.90).toInt()
        val height = metrics.heightPixels

        dialog.getWindow()?.setLayout(width, (4 * height)/5)
    }

     fun closeDialog(){
        fragment.activity?.finish()
        dialog.dismiss()
    }
}