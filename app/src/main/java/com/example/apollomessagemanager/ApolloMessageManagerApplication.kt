package com.example.apollomessagemanager

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ApolloMessageManagerApplication: Application() {
    companion object {
        @JvmStatic
        fun getApplication(context: Context) = context.applicationContext as ApolloMessageManagerApplication
    }
}