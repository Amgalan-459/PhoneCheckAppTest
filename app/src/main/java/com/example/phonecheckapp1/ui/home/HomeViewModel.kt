package com.example.phonecheckapp1.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Добро пожаловать в приложение валидотора телефонного номера"
    }
    val text: LiveData<String> = _text
}