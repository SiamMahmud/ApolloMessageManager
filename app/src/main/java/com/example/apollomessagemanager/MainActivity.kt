package com.example.apollomessagemanager

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.apollomessagemanager.databinding.ActivityMainBinding
import com.example.apollomessagemanager.util.AMMActivityUtil
import com.example.apollomessagemanager.util.SharePreferencesUtil
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception
import javax.inject.Inject
@AndroidEntryPoint
class MainActivity : AppCompatActivity() , AMMActivityUtil.ActivityListener{
    @Inject
    lateinit var sharePrefs: SharePreferencesUtil
    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).apply { setContentView(root) }
        val authedUser: Boolean = try {
            !sharePrefs.getAuthToken().isNullOrEmpty()
        } catch (e: Exception) {
            false
        }
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragmentContainer) as NavHostFragment
        with(navHostFragment) {
            val inflater = findNavController().navInflater
            if (authedUser) {
                findNavController().graph = (inflater.inflate(R.navigation.dashboard_navigation))
            } else {
                findNavController().graph = (inflater.inflate(R.navigation.login_navigation))
            }
        }
        navController = navHostFragment.navController

        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> navHostFragment.findNavController().navigate(R.id.homeFragment)
                R.id.sendMessage -> navHostFragment.findNavController().navigate(R.id.sendTextFragment)
                R.id.contactInfo -> navHostFragment.findNavController().navigate(R.id.contactDetailsFragment)
                R.id.profileInfo -> navHostFragment.findNavController().navigate(R.id.profile)
            }
            true
        }
    }
    override fun hideBottomNavigation(hide: Boolean) {
        if (hide) {
            binding.bottomNavigationView.visibility = View.GONE
        } else {
            binding.bottomNavigationView.visibility = View.VISIBLE
        }
    }

    override fun setFullScreenLoading(short: Boolean) {
        TODO("Not yet implemented")
    }

    override fun closeKeyboard() {
        TODO("Not yet implemented")
    }

    companion object {
        fun getLaunchIntent(context: Context) = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
    }
}
