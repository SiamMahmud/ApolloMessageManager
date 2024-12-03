package com.example.apollomessagemanager.util

interface ISharedPreferencesUtil {
    fun logout()
    fun getAuthToken():String?
    fun setAuthToken(token:String)
}