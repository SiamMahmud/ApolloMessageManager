package com.example.apollomessagemanager.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.apollomessagemanager.R
import com.example.apollomessagemanager.model.PhoneNumber
import com.example.apollomessagemanager.model.SelectedNumber
import com.example.apollomessagemanager.util.SelectedNumbersManager

class SelectedNumberAdapter(private var selectedList: ArrayList<String>) :
    RecyclerView.Adapter<SelectedNumberAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sNumber: TextView
        val removeBtn: ImageView

        init {
            sNumber = itemView.findViewById(R.id.selectNumTV)
            removeBtn = itemView.findViewById(R.id.removeIcon)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.selected_number_info, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return selectedList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = selectedList[position]
        holder.sNumber.text = currentItem
        holder.removeBtn.setOnClickListener {
            selectedList.removeAt(position)
            SelectedNumbersManager.removeNumber(currentItem)
            notifyItemRemoved(position)
        }
    }
}