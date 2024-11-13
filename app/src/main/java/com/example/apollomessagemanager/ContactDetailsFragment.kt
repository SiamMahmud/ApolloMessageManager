package com.example.apollomessagemanager

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.apollomessagemanager.adapter.ContactDetailsAdapter
import com.example.apollomessagemanager.databinding.FragmentContactDetailsBinding
import com.example.apollomessagemanager.model.PhoneNumber
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ContactDetailsFragment : Fragment() {
    private lateinit var binding : FragmentContactDetailsBinding
    private lateinit var contactArray: ArrayList<PhoneNumber>
    private lateinit var adapter: ContactDetailsAdapter
    lateinit var arrayL: Array<String>
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_contact_details, container, false)
        binding.model = this
        binding.backIv.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.contactListRecycle.layoutManager = LinearLayoutManager(activity)
        arrayL = arrayOf()
        contactArray = arrayListOf()
        adapter = ContactDetailsAdapter(contactArray)
        binding.contactListRecycle.adapter = adapter

        binding.selectAllCheckbox.setOnCheckedChangeListener { _, isChecked ->
            adapter.selectAll(isChecked)
        }

        binding.searchContact.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(text: String?): Boolean {
                if (text != null) {
                    searchList(text)
                }
                return true
            }
        })

        binding.btnTransfer.setOnClickListener {
            val selectedContacts = adapter.getSelectedContacts()
            val selectedNumbers = selectedContacts.map { it.pNumber }
            val bundle = Bundle().apply {
                putStringArrayList("s", ArrayList(selectedNumbers))
            }
            findNavController().navigate(R.id.action_contactDetailsFragment_to_selectedNumberFragment, bundle)
        }
        adapter.onUpdateItemClick = {
            var bundle = Bundle()
            bundle.putString("id",it.numberId)
            bundle.putString("pNumber",it.pNumber)
            findNavController().navigate(R.id.action_contactDetailsFragment_to_editContactNumberFragment,bundle)
        }

        adapter.onDeleteItemClick = {
            deleteContactInfo(it)
        }
        return binding.root
    }

    private fun deleteContactInfo(phoneNumber: PhoneNumber) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Delete Number")
        builder.setMessage("Are you sure!")
        builder.setCancelable(false)
        builder.setPositiveButton("Yes"){_,_ ->
            phoneNumber.numberId?.let {numberId ->
                FirebaseDatabase.getInstance().getReference("Phone Number").child(numberId).ref.removeValue()
                    .addOnSuccessListener {
                        loadContactInfo()
                    }.addOnFailureListener {
                        Toast.makeText(context,it.message.toString(),Toast.LENGTH_SHORT).show()
                    }
            }
        }
        builder.setNegativeButton("No"){_,_ ->
            Toast.makeText(context,getString(R.string.cancelled),Toast.LENGTH_SHORT).show()
        }
        val alertDialog = builder.create()
        alertDialog.show()

    }

    private fun searchList(text: String) {
        val searchList = ArrayList<PhoneNumber>()
        for (number in contactArray){
            if (number.pNumber?.lowercase()?.contains(text.lowercase())==true){
                searchList.add(number)
            }
        }
        adapter.searchDataList(searchList)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadContactInfo()
    }

    private fun loadContactInfo() {
        database = FirebaseDatabase.getInstance().getReference("Phone Number")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    contactArray.clear()
                    for (snap in snapshot.children) {
                        val contact = snap.getValue(PhoneNumber::class.java)
                        contact?.numberId = snap.key
                        if (contact != null) {
                            contactArray.add(contact)
                        }
                    }
                    adapter.searchDataList(contactArray)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireActivity(), error.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }


}