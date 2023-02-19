package com.pepperi.printer.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

import com.pepperi.printer.databinding.CardPrinterBinding
import com.pepperi.printer.model.entities.UserPrinter

class ListPrinterAdapter :
    ListAdapter<UserPrinter,ListPrinterAdapter.ListViewHolder>(ListMessageDiffCallback()){
    private var list: List<UserPrinter> = mutableListOf()

        class ListViewHolder(private val binding: CardPrinterBinding, view: View) : RecyclerView.ViewHolder(view){
            fun bind(printer: UserPrinter, position: Int){
                binding.titleCardTxt.text = "Printer ${position} name: ${printer.name}"
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = CardPrinterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding, binding.root)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(list[position], position)
    }

    override fun submitList(submitList: List<UserPrinter>?) {
        submitList?.let {
            list = it
        }
        super.submitList(list)
    }
}

class ListMessageDiffCallback : DiffUtil.ItemCallback<UserPrinter>(){
    override fun areItemsTheSame(oldItem: UserPrinter, newItem: UserPrinter): Boolean {
       return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: UserPrinter, newItem: UserPrinter): Boolean {
        return areItemsTheSame(oldItem, newItem)
    }

}