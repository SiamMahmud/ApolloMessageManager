package com.example.apollomessagemanager

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.example.apollomessagemanager.databinding.FragmentProfileBinding
import com.example.apollomessagemanager.util.SharePreferencesUtil
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class Profile : Fragment() {
    val actionUpdateProfile = Navigation.createNavigateOnClickListener(R.id.action_profile_to_updateProfile)

    @Inject
    lateinit var sharedPrefs: SharePreferencesUtil
    lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        binding.model = this
        binding.btnSignOut.setOnClickListener {
            logout(it)
        }
        return binding.root
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