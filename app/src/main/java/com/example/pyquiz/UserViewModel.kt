package com.example.pyquiz

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {
    val data = MutableLiveData<String>()
    val sharedData = MutableLiveData<String>()

    fun setData(newData : String){
        data.value = newData
    }

    fun shareData(newData : String){
        sharedData.value = newData
    }
}