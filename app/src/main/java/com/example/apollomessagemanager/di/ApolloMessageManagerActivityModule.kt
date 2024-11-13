package com.example.apollomessagemanager.di

import android.app.Activity
import com.example.apollomessagemanager.util.AMMActivityUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object ApolloMessageManagerActivityModule {
    @Provides
    fun providedMGBActivityUtil(activity: Activity): AMMActivityUtil {
        return AMMActivityUtil(activity as AMMActivityUtil.ActivityListener)
    }
}