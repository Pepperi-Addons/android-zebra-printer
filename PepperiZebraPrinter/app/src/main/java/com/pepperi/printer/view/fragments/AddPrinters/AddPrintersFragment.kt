package com.pepperi.printer.view.fragments.AddPrinters

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.pepperi.printer.application.UserApplication
import com.pepperi.printer.databinding.FragmentAddPrintersBinding
import com.pepperi.printer.view.adapters.ListDiscoveredPrinterAdapter
import com.pepperi.printer.viewModel.ViewModelFactory


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class AddPrintersFragment : Fragment() {

    private var _binding: FragmentAddPrintersBinding? = null

    private lateinit var addPrintersModel: AddPrintersViewModel
    private lateinit var viewModelFactory: ViewModelFactory
    private lateinit var userApplication: UserApplication

    private lateinit var listAdapter: ListDiscoveredPrinterAdapter


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userApplication = requireActivity().application as UserApplication

        viewModelFactory = ViewModelFactory(userApplication.repository)
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

        initList()

        initObservers()

        binding.buttonSecond.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE

        }
    }

    private fun initList() {
        listAdapter = ListDiscoveredPrinterAdapter()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}