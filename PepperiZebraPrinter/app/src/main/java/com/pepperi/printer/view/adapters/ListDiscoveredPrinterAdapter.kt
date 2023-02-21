package com.pepperi.printer.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

import com.pepperi.printer.databinding.CardPrinterBinding
import com.zebra.sdk.printer.discovery.DiscoveredPrinter

class ListDiscoveredPrinterAdapter :
    ListAdapter<DiscoveredPrinter,ListDiscoveredPrinterAdapter.ListViewHolder>(ListMessageDiffCallback()){

        class ListViewHolder(private val binding: CardPrinterBinding, view: View) : RecyclerView.ViewHolder(view){
            fun bind(printer: DiscoveredPrinter, position: Int){
                binding.nameCardTxt.text = "${printer.discoveryDataMap?.getValue("FRIENDLY_NAME")}"
                binding.macCardTxt.text = "${printer.discoveryDataMap?.getValue("MAC_ADDRESS")}"
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = CardPrinterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding, binding.root)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

//    override fun submitList(submitList: List<DiscoveredPrinter>?) {
//        submitList?.let {
//            list = it
//        }
//        super.submitList(submitList)
//    }
}

class ListMessageDiffCallback : DiffUtil.ItemCallback<DiscoveredPrinter>(){
    override fun areItemsTheSame(oldItem: DiscoveredPrinter, newItem: DiscoveredPrinter): Boolean {
       return oldItem.address == newItem.address
    }

    override fun areContentsTheSame(oldItem: DiscoveredPrinter, newItem: DiscoveredPrinter): Boolean {
        return areItemsTheSame(oldItem, newItem)
    }

}