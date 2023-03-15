package com.pepperi.printer.view.Managers

import android.app.Dialog
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import com.appa.viewModel.MainViewModel
import com.pepperi.printer.R
import com.pepperi.printer.view.fragments.Main.MainFragment


class PrintDialogManager(val fragment: MainFragment, val viewModel: MainViewModel)  {

    val dialog = Dialog(fragment.requireContext())
    lateinit var pdf_print : TextView
    lateinit var progress_circular : ProgressBar
    lateinit var error_txt : TextView
    lateinit var close_btn : Button

     fun showDialog(text: String){

        dialogSetting()

        initMembers()

        membersSetting(text)

        dialog.show()

    }

    private fun initMembers() {

        pdf_print = dialog.findViewById(R.id.pdf_print) as TextView

        progress_circular = dialog.findViewById(R.id.progress_circular)

        error_txt = dialog.findViewById(R.id.error_txt)

        close_btn = dialog.findViewById(R.id.print_pdf_btn)
    }

    private fun membersSetting(text: String) {

        pdf_print.text = text

        close_btn.setOnClickListener {

            onClick()
        }
    }

    private fun onClick() {

        closeDialog()
    }

    private fun dialogSetting(){
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.pdf_print_show)
    }

    fun showError(text: String){
        pdf_print.text = "Error..."

        progress_circular.visibility = View.GONE

        error_txt.text = text

        error_txt.visibility = View.VISIBLE

        close_btn.visibility = View.VISIBLE
    }

     fun closeDialog(){
        fragment.activity?.finish()
        dialog.dismiss()
    }

}