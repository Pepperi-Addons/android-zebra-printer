package com.pepperi.printer.view.fragments.AddPrinters

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.pepperi.printer.view.Managers.AddUserPrinterDialogManager
import com.pepperi.printer.application.UserApplication
import com.pepperi.printer.databinding.FragmentAddPrintersBinding
import com.pepperi.printer.view.adapters.ListDiscoveredPrinterAdapter
import com.pepperi.printer.viewModel.ViewModelFactory


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class AddPrintersFragment : DialogFragment() {

    private var _binding: FragmentAddPrintersBinding? = null

    private lateinit var addPrintersModel: AddPrintersViewModel
    private lateinit var viewModelFactory: ViewModelFactory
    private lateinit var userApplication: UserApplication

    private lateinit var listAdapter: ListDiscoveredPrinterAdapter

    private var selectedPosition :Int? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userApplication = requireActivity().application as UserApplication

        viewModelFactory = ViewModelFactory(userApplication.repository,userApplication.zebraApi)
        addPrintersModel = ViewModelProvider(this, viewModelFactory).get(AddPrintersViewModel::class.java)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentAddPrintersBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.progressBar.visibility = View.VISIBLE

        setTabSelectedListener()

        initList()

        initObservers()

        binding.addBtn.setOnClickListener {
           selectedPosition?.let {
               showDialog(addPrintersModel.getPrinterByIndex(it)?.name ?:"")
           }
        }
    }


    private fun setTabSelectedListener() {
        binding.tabLayout.addOnTabSelectedListener(object  : OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                onSelectedTab(binding.tabLayout.selectedTabPosition)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })

    }

    private fun onSelectedTab(tabPosition: Int) {
        when(tabPosition){
            0 -> {startScanPrinters()}
            1 -> {}
            2 -> {}
        }
    }

    private fun initList() {
        listAdapter = ListDiscoveredPrinterAdapter()
        listAdapter.setOnItemClickListener(object : ListDiscoveredPrinterAdapter.ClickListener {
            override fun onItemClick(v: View, position: Int) {
                Log.e("onItemClick", "ADD_PERINTER ${position}")
                selectedPosition = position
            }
        })
        binding.printerListRcv.apply {
            layoutManager = LinearLayoutManager(requireContext())  // default layoutManager
            adapter = listAdapter
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

    private fun initObservers() {
        addPrintersModel.discoveredPrintersLiveData.observe(viewLifecycleOwner) { list ->
            Log.e("allPrintersLD.observe", list.toString())
            if (list != null){
                listAdapter.submitList(list)
                checkScreenEmptyList()
            }
           binding.progressBar.visibility = View.GONE
        }
    }

    private fun showDialog(name: String) {
        selectedPosition?.let {
            AddUserPrinterDialogManager(this,addPrintersModel)
                .showDialog(name, it)
        }
    }

    private fun startScanPrinters(){
        binding.progressBar.visibility = View.VISIBLE
        addPrintersModel.scanPrinters()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext()).create()
}