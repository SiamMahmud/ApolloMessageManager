package com.example.apollomessagemanager.di

import com.example.apollomessagemanager.util.ISharedPreferencesUtil
import com.example.apollomessagemanager.util.SharePreferencesUtil
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface OtherInterfacesModule {
    @Binds
    fun bindSharePreferencesUtil(sharePreferencesUtil: SharePreferencesUtil): ISharedPreferencesUtil
}