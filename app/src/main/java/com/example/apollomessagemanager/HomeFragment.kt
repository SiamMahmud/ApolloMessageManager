package com.example.apollomessagemanager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.example.apollomessagemanager.databinding.FragmentHomeBinding
import com.example.apollomessagemanager.util.AMMActivityUtil
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint

class HomeFragment : Fragment() {
    @Inject
    lateinit var activityUtil: AMMActivityUtil
    val actionSendMessage = Navigation.createNavigateOnClickListener(R.id.action_homeFragment_to_sendTextFragment)
    val actionAddNumber = Navigation.createNavigateOnClickListener(R.id.action_homeFragment_to_addNumberFragment)

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        binding.model = this
        activityUtil.hideBottomNavigation(false)
        return binding.root
    }
}