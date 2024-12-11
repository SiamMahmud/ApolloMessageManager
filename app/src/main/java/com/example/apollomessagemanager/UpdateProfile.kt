package com.example.apollomessagemanager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.apollomessagemanager.databinding.FragmentUpdateProfileBinding
import com.example.apollomessagemanager.util.AMMActivityUtil
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class UpdateProfile : Fragment() {

    @Inject
    lateinit var activityUtil: AMMActivityUtil
    private lateinit var binding: FragmentUpdateProfileBinding


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
        return binding.root
    }



}