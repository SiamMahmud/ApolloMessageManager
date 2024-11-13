package com.example.apollomessagemanager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.apollomessagemanager.databinding.FragmentEditContactNumberBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.jakewharton.rxbinding2.widget.RxTextView

class EditContactNumberFragment : Fragment() {

    private lateinit var binding: FragmentEditContactNumberBinding
    private lateinit var database: DatabaseReference


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_edit_contact_number, container, false)
        binding.model = this
        database = Firebase.database.reference
        binding.backIv.setOnClickListener {
            findNavController().popBackStack()
        }
        showNumber()
        binding.btnSave.setOnClickListener {
            updateNumInfo(requireArguments().getString("id"))
        }
        return binding.root
    }

    private fun updateNumInfo(id: String?) {
        var pNumber = binding.numEt.text.toString().trim()
        if (pNumber.isNotEmpty()){
            var numInfo = HashMap<String,Any>()
            numInfo.put("pNumber",pNumber)
            id.let {numberId ->
                database.child("Phone Number").child(numberId!!.toString()).ref.updateChildren(numInfo)
                    .addOnSuccessListener {
                        findNavController().navigate(R.id.action_editContactNumberFragment_to_contactDetailsFragment)
                        Toast.makeText(requireContext(), getString(R.string.update_massage), Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener {
                        Toast.makeText(requireContext(), getString(R.string.update_fail_massage),
                            Toast.LENGTH_SHORT).show()
                    }
            }
        }

    }

    private fun showNumber() {
        binding.numEt.setText(requireArguments().getString("pNumber"))
    }
}