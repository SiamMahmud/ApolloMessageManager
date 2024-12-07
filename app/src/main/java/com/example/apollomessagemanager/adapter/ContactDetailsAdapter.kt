package com.example.apollomessagemanager.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.apollomessagemanager.R
import com.example.apollomessagemanager.model.PhoneNumber
import com.example.apollomessagemanager.util.SelectedNumbersManager

class ContactDetailsAdapter(private var contactList: ArrayList<PhoneNumber>) :
    RecyclerView.Adapter<ContactDetailsAdapter.MyViewHolder>() {
    var onUpdateItemClick: ((PhoneNumber) -> Unit)? = null
    var onDeleteItemClick: ((PhoneNumber) -> Unit)? = null


    fun selectAll(isSelected: Boolean) {

            if (isSelected) {
                for (contact in contactList) {
                    SelectedNumbersManager.addNumber(contact.pNumber)
                }
            } else {
                SelectedNumbersManager.clearNumbers()
            }
            notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.contact_info_items, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return contactList.size
    }

    fun searchDataList(searchList: List<PhoneNumber>) {
        contactList = searchList as ArrayList<PhoneNumber>
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = contactList[position]
        holder.cNumber.text = currentItem.pNumber
        holder.checkbox.isChecked = SelectedNumbersManager.getSelectedNumbers().contains(currentItem.pNumber)
        holder.checkbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                currentItem.pNumber?.let { SelectedNumbersManager.addNumber(it) }
            } else {
                currentItem.pNumber?.let { SelectedNumbersManager.removeNumber(it) }
            }
        }
        holder.editBtn.setOnClickListener {
            onUpdateItemClick?.invoke(currentItem)
        }
        holder.deleteBtn.setOnClickListener {
            onDeleteItemClick?.invoke(currentItem)
        }
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cNumber: TextView
        val editBtn: TextView
        val deleteBtn: TextView
        val checkbox: CheckBox

        init {
            cNumber = itemView.findViewById(R.id.contactTV)
            editBtn = itemView.findViewById(R.id.editTv)
            deleteBtn = itemView.findViewById(R.id.deleteTv)
            checkbox = itemView.findViewById(R.id.checkbox_select_contact)
        }

    }


}

