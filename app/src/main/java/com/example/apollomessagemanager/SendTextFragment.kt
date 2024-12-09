package com.example.apollomessagemanager
import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.SmsManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.apollomessagemanager.databinding.FragmentSendTextBinding
import com.example.apollomessagemanager.util.AMMActivityUtil
import com.example.apollomessagemanager.util.SelectedNumbersManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SendTextFragment : Fragment() {
    @Inject
    lateinit var activityUtil: AMMActivityUtil
    private lateinit var binding: FragmentSendTextBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_send_text, container, false)
        binding.model = this
        binding.addNumIcon.setOnClickListener {
            findNavController().navigate(R.id.action_sendTextFragment_to_contactDetailsFragment)
        }

        binding.viewAll.setOnClickListener {
            findNavController().navigate(R.id.action_sendTextFragment_to_selectedNumberFragment)
        }
        activityUtil.hideBottomNavigation(true)
        binding.backIv.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnSend.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.SEND_SMS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                sendSMS()
            } else {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.SEND_SMS),
                    SMS_PERMISSION_CODE
                )
            }
        }
        return binding.root
    }
    private fun sendSMS() {
        val message = binding.messageEt.text.toString()

        if (message.isEmpty()) {
            Toast.makeText(requireContext(), "Please enter a message", Toast.LENGTH_SHORT).show()
            return
        }
        if (binding.checkboxSelectedNumber.isChecked) {
            val selectedNumbers = SelectedNumbersManager.getSelectedNumbers()
            if (selectedNumbers.isEmpty()) {
                Toast.makeText(requireContext(), "No numbers selected", Toast.LENGTH_SHORT).show()
                return
            }

            val smsManager = SmsManager.getDefault()
            for (phone in selectedNumbers) {
                smsManager.sendTextMessage(phone, null, message, null, null)
            }
            Toast.makeText(requireContext(), "SMS sent to all selected numbers", Toast.LENGTH_SHORT).show()
        } else {
            val phone = binding.numberTV.text.toString()
            if (phone.isNotEmpty()) {
                val smsManager = SmsManager.getDefault()
                smsManager.sendTextMessage(phone, null, message, null, null)
                Toast.makeText(requireContext(), "SMS sent successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Please enter a phone number", Toast.LENGTH_SHORT).show()
            }
        }

        binding.numberTV.setText("")
        binding.messageEt.setText("")
    }
    
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == SMS_PERMISSION_CODE && grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            sendSMS()
        } else {
            Toast.makeText(requireContext(), "Permission Denied!", Toast.LENGTH_SHORT).show()
        }
    }
    companion object {
        private const val SMS_PERMISSION_CODE = 1001
    }



}
