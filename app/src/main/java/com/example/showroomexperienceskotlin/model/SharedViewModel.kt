package com.example.showroomexperienceskotlin.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel: ViewModel() {
    private val _status = MutableLiveData<Int>(0)
    val status: LiveData<Int> = _status

    init {
        resetExperience()
    }

    fun setStatus(newStatus: Int) {
        _status.value = newStatus
    }


    fun resetExperience() {
        _status.value = 0
    }

}