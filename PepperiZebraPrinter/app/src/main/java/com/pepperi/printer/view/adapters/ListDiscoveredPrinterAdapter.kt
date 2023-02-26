package com.pepperi.printer.view.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pepperi.printer.R

import com.pepperi.printer.databinding.CardPrinterBinding
import com.pepperi.printer.model.entities.SelectedPrinterModel

open class ListDiscoveredPrinterAdapter :
    ListAdapter<SelectedPrinterModel,ListDiscoveredPrinterAdapter.ListViewHolder>(ListMessageDiffCallback()){
    var clickListener: ClickListener? = null
      inner class ListViewHolder(private val binding: CardPrinterBinding, view: View) : RecyclerView.ViewHolder(view), View.OnClickListener{

            @SuppressLint("ResourceAsColor")
            fun bind(printer: SelectedPrinterModel, position: Int){
                binding.nameCardTxt.text = "${printer.name}"
                binding.macCardTxt.text = "${printer.mac}"

                binding.getRoot().setOnClickListener(this);

                if(printer.isSelected){
                    binding.mainCard.setBackgroundColor(R.color.white)
                }else{
                    binding.mainCard.setBackgroundColor(R.color.grey)
                }
            }

            override fun onClick(view: View?) {
                if (view != null) {
                    clickListener?.onItemClick(view, adapterPosition)
                }
                for(i in 0 until itemCount){
                    getItem(i).isSelected = false
                }
                getItem(adapterPosition).isSelected = true
                notifyDataSetChanged()
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = CardPrinterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding, binding.root)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }
    open fun setOnItemClickListener(clickListener: ClickListener) {
        this.clickListener = clickListener
    }

    interface ClickListener {
        fun onItemClick(v: View, position: Int)
    }
}

class ListMessageDiffCallback : DiffUtil.ItemCallback<SelectedPrinterModel>(){
    override fun areItemsTheSame(oldItem: SelectedPrinterModel, newItem: SelectedPrinterModel): Boolean {
       return oldItem.mac == newItem.mac
    }

    override fun areContentsTheSame(oldItem: SelectedPrinterModel, newItem: SelectedPrinterModel): Boolean {
        return areItemsTheSame(oldItem, newItem)
    }

}