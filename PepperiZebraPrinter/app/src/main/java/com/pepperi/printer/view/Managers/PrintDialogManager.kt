package com.pepperi.printer.view.Managers

import android.app.Dialog
import android.view.Window
import android.widget.TextView
import com.appa.viewModel.MainViewModel
import com.pepperi.printer.R
import com.pepperi.printer.view.fragments.Main.MainFragment


class PrintDialogManager(val fragment: MainFragment, val viewModel: MainViewModel) {

    val dialog = Dialog(fragment.requireContext())
    lateinit var pdf_print : TextView

     fun showDialog(text: String){

        dialogSetting()

        initMembers()

        membersSetting(text)


        dialog.show()

    }

    private fun initMembers() {

        pdf_print = dialog.findViewById(R.id.pdf_print) as TextView
    }

    private fun membersSetting(text: String) {

        pdf_print.text = text
    }

    private fun dialogSetting(){
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.pdf_print_show)
    }

    fun closeDialog(){
        fragment.activity?.finish()
        dialog.dismiss()
    }
}