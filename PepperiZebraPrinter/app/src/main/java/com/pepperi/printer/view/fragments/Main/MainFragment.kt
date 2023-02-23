package com.pepperi.printer.view.fragments.Main

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
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
import com.pepperi.printer.view.adapters.ListPrinterAdapter
import com.pepperi.printer.viewModel.ViewModelFactory
import com.zebra.sdk.comm.BluetoothConnectionInsecure
import com.zebra.sdk.comm.Connection
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
const val BLUETOOTH_PERMISSION = 100

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null

    private lateinit var mainViewModel: MainViewModel
    private lateinit var viewModelFactory: ViewModelFactory
    private lateinit var userApplication: UserApplication

    private lateinit var listAdapter: ListPrinterAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userApplication = requireActivity().application as UserApplication

        viewModelFactory = ViewModelFactory(userApplication.repository)
        mainViewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)

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
                printB()
            }

        }

        return binding.root

    }

    private  suspend fun printB() {
        try {
            val thePrinterConn: Connection = BluetoothConnectionInsecure("AC:3F:A4:4B:50:83")
            coroutineScope() {
                // Open the connection - physical connection is established here.
                // Open the connection - physical connection is established here.
                thePrinterConn.open()
                Log.e("thePrinterConn", "start")
                // This example prints "This is a ZPL test." near the top of the label.

                // This example prints "This is a ZPL test." near the top of the label.
                val zplData = "^XA^FO20,20^A0N,25,25^FDThis is a ZPL test.^FS^XZ"

                // Send the data to printer as a byte array.

                // Send the data to printer as a byte array.
                thePrinterConn.write(zplData.toByteArray())
            }
            Log.e("thePrinterConn", "close")
            thePrinterConn.close()
        }catch (e : Exception){
            Log.e("printException", e.stackTraceToString())
        }
    }


    private fun initList() {
        listAdapter = ListPrinterAdapter()

        listAdapter.setOnItemClickListener(object : ListPrinterAdapter.ClickListener{
            override fun onItemClick(v: View, position: Int) {

                val popup = PopupMenu(requireContext(), v)
                val inflater: MenuInflater = popup.menuInflater
                inflater.inflate(R.menu.menu_user_printer, popup.menu)
                popup.show()
            }

        })
        binding.printerListRcv.apply {
            layoutManager = LinearLayoutManager(requireContext())  // default layoutManager
            adapter = listAdapter
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
}