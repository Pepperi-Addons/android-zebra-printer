package com.pepperi.printer.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

import com.pepperi.printer.databinding.CardPrinterBinding
import com.pepperi.printer.model.entities.UserPrinter
import com.zebra.sdk.printer.discovery.DiscoveredPrinter

class ListDiscoveredPrinterAdapter :
    ListAdapter<UserPrinter,ListDiscoveredPrinterAdapter.ListViewHolder>(ListMessageDiffCallback()){

        class ListViewHolder(private val binding: CardPrinterBinding, view: View) : RecyclerView.ViewHolder(view){
            fun bind(printer: UserPrinter, position: Int){
                binding.nameCardTxt.text = "${printer.name}"
                binding.macCardTxt.text = "${printer.mac}"
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = CardPrinterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding, binding.root)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }
}

class ListMessageDiffCallback : DiffUtil.ItemCallback<UserPrinter>(){
    override fun areItemsTheSame(oldItem: UserPrinter, newItem: UserPrinter): Boolean {
       return oldItem.mac == newItem.mac
    }

    override fun areContentsTheSame(oldItem: UserPrinter, newItem: UserPrinter): Boolean {
        return areItemsTheSame(oldItem, newItem)
    }

}