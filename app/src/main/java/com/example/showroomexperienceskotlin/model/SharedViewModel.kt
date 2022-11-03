package com.example.showroomexperienceskotlin.model

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import com.example.showroomexperienceskotlin.R
import com.robotemi.sdk.Robot
import com.robotemi.sdk.listeners.OnDetectionStateChangedListener
import com.robotemi.sdk.listeners.OnGoToLocationStatusChangedListener


class SharedViewModel: ViewModel() {
    private val TAG = "SharedViewModel"
    private val _status = MutableLiveData<String>("Inicio")
    val status: LiveData<String> = _status

    private var _robot = Robot.getInstance()
    val robot : Robot = _robot

    init {
        resetExperience()
    }

    fun setStatus(newStatus: String) {
        _status.value = newStatus
    }

    fun initDetectionMode(onDetectionStateChangedListener: OnDetectionStateChangedListener) {
        _robot.setDetectionModeOn(true, 0.8F)
        _robot.addOnDetectionStateChangedListener(onDetectionStateChangedListener)
        if(_robot.detectionModeOn) Log.i(TAG, "Se ha activado el modo deteccion")
    }

    fun initGoToLocationMode(onGoToLocationStatusChangedListener : OnGoToLocationStatusChangedListener) {
        _robot.addOnGoToLocationStatusChangedListener(onGoToLocationStatusChangedListener)
    }

    fun finishDetectionMode(onDetectionStateChangedListener: OnDetectionStateChangedListener) {
        _robot.removeOnDetectionStateChangedListener(onDetectionStateChangedListener)
    }

    fun finishGoToLocationMode(onGoToLocationStatusChangedListener : OnGoToLocationStatusChangedListener) {
        _robot.removeOnGoToLocationStatusChangedListener(onGoToLocationStatusChangedListener)
    }

    fun goTo(location: String) {
        _robot.goTo(location)
    }

    fun resetExperience() {
        _status.value = "Inicio"
    }
}