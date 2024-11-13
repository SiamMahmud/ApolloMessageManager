package com.example.apollomessagemanager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.apollomessagemanager.adapter.SelectedNumberAdapter
import com.example.apollomessagemanager.databinding.FragmentSelectedNumberBinding
import com.example.apollomessagemanager.model.SelectedNumber
import com.example.apollomessagemanager.util.AMMActivityUtil


class SelectedNumberFragment : Fragment() {
    private lateinit var binding : FragmentSelectedNumberBinding
    private lateinit var selectNumArray: ArrayList<String>
    private lateinit var adapter: SelectedNumberAdapter
    lateinit var activityUtil : AMMActivityUtil


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_selected_number, container, false)
        binding.model = this
        binding.backIv.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.numberListRecycle.layoutManager = LinearLayoutManager(activity)
        selectNumArray = arguments?.getStringArrayList("s") ?:  ArrayList()
        adapter = SelectedNumberAdapter(selectNumArray)
        binding.numberListRecycle.adapter = adapter
        return binding.root

    }
}