package com.pepperi.printer.view.Managers

import android.app.Dialog
import android.util.Base64
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.appa.viewModel.MainViewModel
import com.github.barteksc.pdfviewer.PDFView
import com.pepperi.printer.R
import com.pepperi.printer.view.fragments.Main.MainFragment
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.io.File

class PrintDialogManager(val fragment: MainFragment, val viewModel: MainViewModel) {

    val dialog = Dialog(fragment.requireContext())
    lateinit var pdfView_pvi : PDFView
    lateinit var print_pdf_btn : Button
    lateinit var pdf_print : TextView

     fun showDialog(text: String){

        dialogSetting()

        initMembers()

        membersSetting(text)


        dialog.show()

    }

    private fun initMembers() {

        pdfView_pvi = dialog.findViewById(R.id.pdfView) as PDFView

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

        fragment.findNavController().popBackStack()
        dialog.dismiss()
    }


    private fun dialogSetting(){
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.pdf_print_show)
    }
}