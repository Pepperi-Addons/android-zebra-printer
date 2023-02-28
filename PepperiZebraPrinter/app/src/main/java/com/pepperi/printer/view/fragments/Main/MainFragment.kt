package com.pepperi.printer.view.fragments.Main

import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.*
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.Toolbar.OnMenuItemClickListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.appa.viewModel.MainViewModel
import com.pepperi.printer.R
import com.pepperi.printer.application.UserApplication
import com.pepperi.printer.databinding.FragmentMainBinding
import com.pepperi.printer.view.Managers.BluetoothPermissionManager
import com.pepperi.printer.view.Managers.PrintDialogManager
import com.pepperi.printer.view.adapters.ListPrinterAdapter
import com.pepperi.printer.viewModel.ViewModelFactory
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null

    private lateinit var mainViewModel: MainViewModel
    private lateinit var viewModelFactory: ViewModelFactory
    private lateinit var userApplication: UserApplication

    private lateinit var listAdapter: ListPrinterAdapter
    private var selectedPrinter :Int? = null


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userApplication = requireActivity().application as UserApplication

        viewModelFactory = ViewModelFactory(userApplication.repository)
        mainViewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)


        val action = activity?.intent?.action
        val data = activity?.intent?.data

        action?.let { _action ->
            data?.let{ _data ->

                printData(_data)

            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMainBinding.inflate(inflater, container, false)

        BluetoothPermissionManager(this).requestPermissions()

        initList()

        initObservers()

        binding.printBtn.setOnClickListener {
            lifecycleScope.launch {
                mainViewModel.print(mainViewModel.allPrintersLiveData.value!![mainViewModel.userDefaultPrinter!!].mac,getFile(mainViewModel.gethardPDF()))
            }
        }

        return binding.root

    }

    private fun initList() {
        listAdapter = ListPrinterAdapter()

        listAdapter.setOnItemClickListener(object : ListPrinterAdapter.ClickListener{
            override fun onItemClick(v: View, position: Int) {

                selectedPrinter = position

                val popup = PopupMenu(requireContext(), v)
                val inflater: MenuInflater = popup.menuInflater
                inflater.inflate(R.menu.menu_user_printer, popup.menu)
                popup.setOnMenuItemClickListener(object : OnMenuItemClickListener,
                    PopupMenu.OnMenuItemClickListener {
                    override fun onMenuItemClick(item: MenuItem): Boolean {
                        return when (item.itemId) {
                            R.id.menu_action_default -> {
                                selectedPrinter?.let {
                                    Log.e("menu_action_remove", "start")
                                    mainViewModel.userDefaultPrinter = selectedPrinter
                                    setUserPrinterAsDefault(mainViewModel.userDefaultPrinter)
                                }
                                true
                            }
                            R.id.menu_action_remove -> {
                                selectedPrinter?.let {
                                    Log.e("menu_action_remove", "start")
                                    removeUserPrinter(it)
                                }
                                true
                            }
                            else -> false
                        }
                    }
                })
                popup.show()
            }

        })
        binding.printerListRcv.apply {
            layoutManager = LinearLayoutManager(requireContext())  // default layoutManager
            adapter = listAdapter
        }
    }

    private fun removeUserPrinter(selectedPrinter: Int) {

        mainViewModel.removeUserPrinter(selectedPrinter)

        listAdapter.notifyItemRemoved(selectedPrinter)

        mainViewModel.getAllUserPrinters()
    }

    private fun setUserPrinterAsDefault(defaultPrinter: Int?) {
        defaultPrinter?.let {

            mainViewModel.setUserPrinterAsDefault(it)

            mainViewModel.getAllUserPrinters()
        }
    }

    private fun initObservers() {
        mainViewModel.allPrintersLiveData.observe(viewLifecycleOwner) { list ->
            Log.e("allPrintersLD.observe", list.toString())
            if (list!=null){
                listAdapter.submitList(list)
                listAdapter.notifyDataSetChanged()
                checkScreenEmptyList()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_MainFragment_to_SecondFragment)
        }
    }


    private fun checkScreenEmptyList() {
        if (listAdapter.itemCount ==  0){
            binding.printerListRcv.visibility = View.GONE
            binding.noPrinterTxt.visibility = View.VISIBLE
        }else{
            binding.printerListRcv.visibility = View.VISIBLE
            binding.noPrinterTxt.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()

        mainViewModel.getAllUserPrinters()
    }

    private fun printData(data: Uri) {

        mainViewModel.userDefaultPrinter?.let { defaultPrinter ->

            val parameters = data.queryParameterNames.associateWith { data.getQueryParameters(it) }

            val printerDialogManager = PrintDialogManager(this,mainViewModel)

            val dataURI = parameters["dataURI"]?.get(0).toString()

            val dataToPrint = getDataToPrint(dataURI)

            printerDialogManager.showDialog("Printing: ${dataToPrint.toByteArray().decodeToString()}")

            if (dataURI.contains("x-application/zpl")
                || dataURI.contains("application/vnd.hp-PCL")){

                lifecycleScope.launch{
                    mainViewModel.printZPL(mainViewModel.allPrintersLiveData.value!![defaultPrinter].mac,dataToPrint)
                }

            }else if(dataURI.contains("application/pdf")) {
                lifecycleScope.launch{
                    mainViewModel.print(mainViewModel.allPrintersLiveData.value!![defaultPrinter].mac,getFile(dataToPrint))
                }
            } else {

            }
        }?: Log.e("Print error", "No printer selected")
    }

    fun getFile(dataPrint:String) : File{
        val path = requireActivity().filesDir
        val file = File(path, "test_pdf.pdf")
        try {
            Log.e("getFile",file.path)
            val outputStreamWriter = FileOutputStream(file)
            val decodedString = Base64.decode(dataPrint, Base64.DEFAULT)
            outputStreamWriter.write(decodedString)
            outputStreamWriter.close()
        } catch (e: IOException) {
            Log.e("Exception", "File write failed: $e")
        }
        return file

    }

    private fun getDataToPrint(dataURI: String): String {

        val dataArryeHelper = dataURI.split(",")

        var dataStringHelper = ""

        for (i in 1 until dataArryeHelper.size){
            dataStringHelper += dataArryeHelper[i]
        }

        return dataStringHelper
    }
}