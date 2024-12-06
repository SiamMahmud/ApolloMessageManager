package com.example.apollomessagemanager.util

object SelectedNumbersManager {
    private val selectedNumbers = mutableListOf<String>()

    fun getSelectedNumbers(): List<String> = selectedNumbers

    fun addNumber(number: String) {
        if (!selectedNumbers.contains(number)) {
            selectedNumbers.add(number)
        }
    }
    fun removeNumber(number: String) {
        selectedNumbers.remove(number)
    }
    fun clearNumbers() {
        selectedNumbers.clear()
    }
}