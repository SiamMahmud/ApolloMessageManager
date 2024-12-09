package com.example.apollomessagemanager

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.apollomessagemanager.databinding.FragmentAddNumberBinding
import com.example.apollomessagemanager.model.PhoneNumber
import com.example.apollomessagemanager.util.AMMActivityUtil
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.jakewharton.rxbinding2.widget.RxTextView
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AddNumberFragment : Fragment() {

    @Inject
    lateinit var activityUtil: AMMActivityUtil
    private lateinit var binding: FragmentAddNumberBinding
    lateinit var auth: FirebaseAuth
    lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_number, container, false)
        binding.model = this
        activityUtil.hideBottomNavigation(true)
        binding.backIv.setOnClickListener {
            findNavController().popBackStack()
        }

        auth = Firebase.auth
        database = Firebase.database.reference

        val phoneNumberStream = RxTextView.textChanges(binding.phoneNumberEt)
            .skipInitialValue()
            .map { phone ->
                phone.length < 10
            }
        phoneNumberStream.subscribe {
            binding.phoneNumberEt.error = if (it) getString(R.string.error_number) else null
        }

        binding.btnSave.setOnClickListener {
            val countryCode = binding.ccpGetFullNumber.selectedCountryCodeWithPlus
            val phoneNumber = binding.phoneNumberEt.text.toString().trim()
            val fullNumber = "$countryCode$phoneNumber"

            database = FirebaseDatabase.getInstance().getReference("Phone Number")
            val numId = database.push().key ?: return@setOnClickListener
            val adminBloodBank = PhoneNumber(
                pNumber = fullNumber
            )
            database.child(numId).setValue(adminBloodBank).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    showSuccessDialog()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Failed to add entry. Please try again.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        return binding.root
    }

    private fun showSuccessDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Success")
            .setMessage("Number Added Successfully! Would you like to add more data?")
            .setPositiveButton("Yes") { dialog, _ ->
                dialog.dismiss()
                binding.phoneNumberEt.text?.clear()
            }
            .create()
            .show()
    }
}
