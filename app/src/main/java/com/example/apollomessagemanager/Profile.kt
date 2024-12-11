package com.example.apollomessagemanager

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.example.apollomessagemanager.databinding.FragmentProfileBinding
import com.example.apollomessagemanager.util.AMMActivityUtil
import com.example.apollomessagemanager.util.SharePreferencesUtil

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@AndroidEntryPoint
class Profile : Fragment() {
    val actionUpdateProfile = Navigation.createNavigateOnClickListener(R.id.action_profile_to_updateProfile)

    @Inject
    lateinit var activityUtil: AMMActivityUtil
    @Inject
    lateinit var sharedPrefs: SharePreferencesUtil
    lateinit var binding: FragmentProfileBinding
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        binding.model = this

        binding.btnSignOut.setOnClickListener {
            logout(it)
        }

        database = FirebaseDatabase.getInstance().getReference("User")

        loadUserData()

        return binding.root
    }

    private fun loadUserData() {
        activityUtil.setFullScreenLoading(true)
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                activityUtil.setFullScreenLoading(false)
                val name = snapshot.child("name").getValue(String::class.java)
                val phone = snapshot.child("phone").getValue(String::class.java)

                if (name != null && phone != null) {
                    binding.fullNameTv.text = name
                    binding.phoneNumberTv.text = phone
                } else {
                    Toast.makeText(context, "Data not found", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Failed to load data: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun logout(it: View?) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Logout")
        builder.setMessage("Are you sure!")
        builder.setCancelable(false)
        builder.setPositiveButton("Yes") { _, _ ->
            sharedPrefs.setAuthToken("")
            activity?.let {
                startActivity(MainActivity.getLaunchIntent(it))
            }
        }
        builder.setNegativeButton("No") { _, _ -> }
        val alartDialog = builder.create()
        alartDialog.show()

    }

}