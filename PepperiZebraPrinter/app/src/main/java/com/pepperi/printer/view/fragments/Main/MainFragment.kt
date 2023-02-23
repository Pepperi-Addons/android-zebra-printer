package com.pepperi.printer.view.fragments.Main

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.appa.viewModel.MainViewModel
import com.pepperi.printer.R
import com.pepperi.printer.view.Managers.BluetoothPermissionManager
import com.pepperi.printer.application.UserApplication
import com.pepperi.printer.databinding.FragmentMainBinding
import com.pepperi.printer.view.adapters.ListPrinterAdapter
import com.pepperi.printer.viewModel.ViewModelFactory


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

        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        viewModelFactory = ViewModelFactory(userApplication.repository,sharedPref)
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



        return binding.root

    }





    private fun initList() {
        listAdapter = ListPrinterAdapter()
        binding.printerListRcv.apply {
            layoutManager = LinearLayoutManager(requireContext())  // default layoutManager
            adapter = listAdapter
        }
    }

    private fun initObservers() {
        mainViewModel.allPrintersLiveData.observe(viewLifecycleOwner) { list ->
            Log.e("allPrintersLD.observe", list.toString())

            listAdapter.submitList(list)
            checkScreenEmptyList()
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