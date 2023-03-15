package com.pepperi.printer.view.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pepperi.printer.R
import com.pepperi.printer.databinding.CardUserPrinterBinding
import com.pepperi.printer.model.entities.UserPrinterModel

open class ListPrinterAdapter(val context: Context) :
 
    ListAdapter<UserPrinterModel,ListPrinterAdapter.ListViewHolder>(ListUserPrinterDiffCallback()){

    var clickListener: ClickListener? = null

        inner class ListViewHolder(private val binding: CardUserPrinterBinding, view: View) : RecyclerView.ViewHolder(view), View.OnClickListener{
            @SuppressLint("ResourceAsColor")
            fun bind(printer: UserPrinterModel, position: Int){

                binding.frandlyNameCardTxt.text = printer.friendly_name
                binding.nameCardTxt.text = printer.name
                binding.macCardTxt.text = printer.mac

                binding.getRoot().setOnClickListener(this);

                if(printer.isDefault){
                    binding.mainCard.setBackgroundColor(ContextCompat.getColor(context, R.color.teal_700_lite))
                }else{
                    binding.mainCard.setBackgroundColor(ContextCompat.getColor(context, R.color.teal_700))
                }
            }

            override fun onClick(view: View?) {
                Log.e("onClick","adapterPosition ${adapterPosition} , $view.id}")
                if (view != null) {
                    clickListener?.onItemClick(view, getItem(adapterPosition))
                    Log.e("onClick","adapterPosition ${adapterPosition} , $view.id}")
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = CardUserPrinterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding, binding.root)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }
    open fun setOnItemClickListener(clickListener: ClickListener) {
        this.clickListener = clickListener
    }

    interface ClickListener {
        fun onItemClick(v: View, userPrinter: UserPrinterModel)
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
