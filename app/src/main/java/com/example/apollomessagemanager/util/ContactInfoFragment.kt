package com.example.apollomessagemanager.util

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.apollomessagemanager.MainActivity
import com.example.apollomessagemanager.R
import com.example.apollomessagemanager.databinding.FragmentContactInfoBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ContactInfoFragment : Fragment() {
    @Inject
    lateinit var sharedPrefs: SharePreferencesUtil
    lateinit var binding: FragmentContactInfoBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_contact_info, container, false)
        binding.model = this
        binding.btnSignOut.setOnClickListener {
            logout(it)
        }
        return binding.root
    }
    private fun logout(view: View) {
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