package com.example.apollomessagemanager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.apollomessagemanager.adapter.ContactDetailsAdapter
import com.example.apollomessagemanager.databinding.FragmentSendTextBinding
import com.example.apollomessagemanager.util.AMMActivityUtil
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SendTextFragment : Fragment() {
    @Inject
    lateinit var activityUtil: AMMActivityUtil
    private lateinit var binding: FragmentSendTextBinding
    private lateinit var adapter: ContactDetailsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_send_text, container, false)
        binding.model = this
        binding.addNumIcon.setOnClickListener {
            findNavController().navigate(R.id.action_sendTextFragment_to_contactDetailsFragment)
        }
        binding.btnSelectedNumber.setOnClickListener {
            findNavController().navigate(R.id.action_sendTextFragment_to_selectedNumberFragment)
        }
        activityUtil.hideBottomNavigation(true)
        binding.backIv.setOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }


}
