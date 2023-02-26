package com.pepperi.printer.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

import com.pepperi.printer.databinding.CardPrinterBinding
import com.pepperi.printer.model.entities.SelectedPrinterModel
import com.pepperi.printer.model.entities.UserPrinterModel

class ListPrinterAdapter :
 
    ListAdapter<UserPrinterModel,ListPrinterAdapter.ListViewHolder>(ListUserPrinterDiffCallback()){

        class ListViewHolder(private val binding: CardPrinterBinding, view: View) : RecyclerView.ViewHolder(view){
            fun bind(printer: UserPrinterModel, position: Int){
                binding.nameCardTxt.text = "Printer ${position} name: ${printer.name}"
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

class ListUserPrinterDiffCallback : DiffUtil.ItemCallback<UserPrinterModel>(){
    override fun areItemsTheSame(oldItem: UserPrinterModel, newItem: UserPrinterModel): Boolean {
        return oldItem.mac == newItem.mac
    }

    override fun areContentsTheSame(oldItem: UserPrinterModel, newItem: UserPrinterModel): Boolean {
        return areItemsTheSame(oldItem, newItem)
    }

}
