package com.example.apollomessagemanager.login

import android.os.Bundle
import android.os.Handler
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.example.apollomessagemanager.MainActivity
import com.example.apollomessagemanager.R
import com.example.apollomessagemanager.databinding.FragmentLoginInputBinding
import com.example.apollomessagemanager.util.AMMActivityUtil
import com.example.apollomessagemanager.util.SharePreferencesUtil
import com.jakewharton.rxbinding2.widget.RxTextView
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Observable
import javax.inject.Inject
@AndroidEntryPoint
class LoginInputFragment : Fragment() {
    @Inject
    lateinit var activityUtil : AMMActivityUtil
    @Inject
    lateinit var sharedPrefs: SharePreferencesUtil
    val actionForgotPassword = Navigation.createNavigateOnClickListener(R.id.action_loginInputFragment_to_loginForgotPasswordFragment)
    private lateinit var binding: FragmentLoginInputBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login_input, container, false)
        binding.model = this
        activityUtil.hideBottomNavigation(true)
        isEnableSignInButton(false)
        val emailStream = RxTextView.textChanges(binding.emailEt)
            .skipInitialValue()
            .map { email ->
                !Patterns.EMAIL_ADDRESS.matcher(email).matches()
            }
        emailStream.subscribe {
            binding.emailEt.error = if (it) getString(R.string.error_email) else null
        }

        val passwordStream = RxTextView.textChanges(binding.passwordEt)
            .skipInitialValue()
            .map { password ->
                password.isEmpty()
            }

        val invalidFiledStream = Observable.combineLatest(
            emailStream,
            passwordStream
        ) { emailInvalid: Boolean, passwordInvalid: Boolean ->
            !emailInvalid && !passwordInvalid
        }
        invalidFiledStream.subscribe { isValid ->
            isEnableSignInButton(isValid)
        }
        binding.loginErrorTv.visibility = View.GONE
        binding.btnSignIn.setOnClickListener {

            Handler().postDelayed({
                sharedPrefs.setAuthToken("Rakib")
                activity?.let {
                    startActivity(MainActivity.getLaunchIntent(it))
                }
            }, 3000)
        }
        return binding.root
    }
    private fun isEnableSignInButton(isEnable: Boolean) {
        if (isEnable) {
            binding.btnSignIn.isEnabled = true
            binding.btnSignIn.backgroundTintList =
                ContextCompat.getColorStateList(requireActivity(), R.color.blue_300)
        } else {
            binding.btnSignIn.isEnabled = true
            binding.btnSignIn.backgroundTintList =
                ContextCompat.getColorStateList(requireActivity(), R.color.blue_500)
        }
    }
}