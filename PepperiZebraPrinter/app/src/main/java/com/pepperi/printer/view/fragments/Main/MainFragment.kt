package com.pepperi.printer.view.fragments.Main

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.appa.viewModel.MainViewModel
import com.pepperi.printer.R
import com.pepperi.printer.application.UserApplication
import com.pepperi.printer.databinding.FragmentMainBinding
import com.pepperi.printer.view.adapters.ListDiscoveredPrinterAdapter
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

    private lateinit var permissionLauncher : ActivityResultLauncher<Array<String>>

    private var isBluetoothScanPermissionGranted = false
    private var isBluetoothConnectPermissionGranted = false
    private var isBluetoothAdminPermissionGranted = false
    private var isBluetoothAdvertisePermissionGranted = false
    private var isBluetoothPermissionGranted = false

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

        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){ permission ->

            isBluetoothPermissionGranted = permission[Manifest.permission.BLUETOOTH] ?: isBluetoothPermissionGranted
            isBluetoothAdminPermissionGranted = permission[Manifest.permission.BLUETOOTH_ADMIN] ?: isBluetoothAdminPermissionGranted

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                isBluetoothScanPermissionGranted = permission[Manifest.permission.BLUETOOTH_SCAN] ?: isBluetoothScanPermissionGranted
                isBluetoothConnectPermissionGranted = permission[Manifest.permission.BLUETOOTH_CONNECT] ?: isBluetoothConnectPermissionGranted
                isBluetoothAdvertisePermissionGranted = permission[Manifest.permission.BLUETOOTH_ADVERTISE] ?: isBluetoothAdvertisePermissionGranted
            }else{
                Log.e("PermissionGranted", "older permission needed")
            }
        }

        requestBluetoothPermission()

        initList()

        initObservers()



        return binding.root

    }


    private fun requestBluetoothPermission() {

        Log.e("requestBluetoothPermiss", "stzrt requestBluetoothPermission")

        isBluetoothPermissionGranted = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.BLUETOOTH,
        ) == PackageManager.PERMISSION_GRANTED

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            isBluetoothScanPermissionGranted = ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.BLUETOOTH_SCAN,
            ) == PackageManager.PERMISSION_GRANTED

            isBluetoothConnectPermissionGranted = ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.BLUETOOTH_CONNECT,
            ) == PackageManager.PERMISSION_GRANTED

            isBluetoothAdvertisePermissionGranted = ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.BLUETOOTH_ADVERTISE,
            ) == PackageManager.PERMISSION_GRANTED
        }else{
            Log.e("getBluetoothPermission", "older permission needed")
        }

        isBluetoothAdminPermissionGranted = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.BLUETOOTH_ADMIN,
        ) == PackageManager.PERMISSION_GRANTED

        val permissionRequest : MutableList<String> = ArrayList()

        if (!isBluetoothPermissionGranted){
            permissionRequest.add(Manifest.permission.BLUETOOTH)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!isBluetoothScanPermissionGranted) {
                permissionRequest.add(Manifest.permission.BLUETOOTH_SCAN)
            }
            if (!isBluetoothConnectPermissionGranted) {
                permissionRequest.add(Manifest.permission.BLUETOOTH_CONNECT)
            }
            if (!isBluetoothAdvertisePermissionGranted) {
                permissionRequest.add(Manifest.permission.BLUETOOTH_ADVERTISE)
            }
        }
        if (!isBluetoothAdminPermissionGranted){
            permissionRequest.add(Manifest.permission.BLUETOOTH_ADMIN)
        }

        if (permissionRequest.isNotEmpty()){
            permissionLauncher.launch(permissionRequest.toTypedArray())
        }


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//            requestPermissions(arrayOf(
//                Manifest.permission.BLUETOOTH_SCAN,
//                Manifest.permission.BLUETOOTH_CONNECT,
//                Manifest.permission.BLUETOOTH_ADMIN,
//                Manifest.permission.BLUETOOTH_ADVERTISE,
//                Manifest.permission.ACCESS_COARSE_LOCATION,
//                Manifest.permission.BLUETOOTH
//            ),0)
//        }
//        else{
//            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
//            startActivityForResult(enableBtIntent, BLUETOOTH_PERMISSION)
//        }

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

//            listAdapter.submitList(list)
//            checkScreenEmptyList()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_MainFragment_to_SecondFragment)
        }

        checkScreenEmptyList()
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
}