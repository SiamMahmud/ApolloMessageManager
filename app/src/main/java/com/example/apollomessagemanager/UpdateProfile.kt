package com.example.apollomessagemanager


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.apollomessagemanager.databinding.FragmentUpdateProfileBinding
import com.example.apollomessagemanager.util.AMMActivityUtil
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.jakewharton.rxbinding2.widget.RxTextView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class UpdateProfile : Fragment() {
    @Inject
    lateinit var activityUtil: AMMActivityUtil
    private lateinit var binding: FragmentUpdateProfileBinding
    lateinit var database: DatabaseReference


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_update_profile, container, false)
        binding.model = this
        activityUtil.hideBottomNavigation(true)

        binding.backIv.setOnClickListener {
            findNavController().popBackStack()
        }
        database = Firebase.database.reference

        val fullNameStream = RxTextView.textChanges(binding.fullNameEt)
            .skipInitialValue()
            .map { name ->
                name.isEmpty()
            }
        fullNameStream.subscribe {
            binding.fullNameEt.error = if (it) getString(R.string.error_name) else null }

        val phoneNumberStream = RxTextView.textChanges(binding.phoneNumberEt)
            .skipInitialValue()
            .map { phone ->
                phone.length < 11
            }
        phoneNumberStream.subscribe {
            binding.phoneNumberEt.error = if (it) getString(R.string.error_number) else null }


        val invalidFiledStream = io.reactivex.Observable.combineLatest(
            fullNameStream,
            phoneNumberStream
        ) { nameInvalid: Boolean, phoneInvalid: Boolean ->
            !nameInvalid && !phoneInvalid
        }

        invalidFiledStream.subscribe { isValid ->
            isEnableSaveButton(isValid) }

        binding.saveTv.setOnClickListener {
            activityUtil.setFullScreenLoading(true)
            val user = mapOf(
                "name" to binding.fullNameEt.text!!.toString().trim(),
                "phone" to binding.phoneNumberEt.text!!.toString().trim()
            )

            database.child("User").setValue(user)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        activityUtil.setFullScreenLoading(false)
                        findNavController().navigate(R.id.action_updateProfile_to_profile)
                        binding.fullNameEt.text!!.clear()
                        binding.phoneNumberEt.text!!.clear()
                    } else {
                        activityUtil.setFullScreenLoading(false)
                        Toast.makeText(activity, getText(R.string.error_message), Toast.LENGTH_SHORT).show()

                    }
                }
        }
        database = FirebaseDatabase.getInstance().getReference("User")
        showToData()
        return binding.root
    }

    private fun showToData() {
        activityUtil.setFullScreenLoading(true)
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                activityUtil.setFullScreenLoading(false)

                val name = snapshot.child("name").getValue(String::class.java)
                val phone = snapshot.child("phone").getValue(String::class.java)

                if (name != null && phone != null) {
                    binding.fullNameEt.setText(name)  // Use setText to update TextInputEditText
                    binding.phoneNumberEt.setText(phone)
                } else {
                    Toast.makeText(context, "Data not found", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Failed to load data: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun isEnableSaveButton(isEnable: Boolean) {
        if (isEnable == true) {
            binding.saveTv.isEnabled = true
            binding.saveTv.backgroundTintList = ContextCompat.getColorStateList(requireActivity(), R.color.colorPrimary)
        } else {
            binding.saveTv.isEnabled = false
            binding.saveTv.backgroundTintList = ContextCompat.getColorStateList(requireActivity(), R.color.blue_500)
        }

    }


}