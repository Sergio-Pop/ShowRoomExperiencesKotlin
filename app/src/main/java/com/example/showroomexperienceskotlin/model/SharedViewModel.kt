package com.example.showroomexperienceskotlin.model

import android.content.Context
import android.net.Uri
import android.provider.ContactsContract.Directory.PACKAGE_NAME
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import com.example.showroomexperienceskotlin.MainActivity
import com.example.showroomexperienceskotlin.R
import com.example.showroomexperienceskotlin.TTSManager
import com.robotemi.sdk.Robot
import com.robotemi.sdk.listeners.OnDetectionStateChangedListener
import com.robotemi.sdk.listeners.OnGoToLocationStatusChangedListener


class SharedViewModel: ViewModel() {
    private val TAG = "SharedViewModel"
    private val _status = MutableLiveData<Int>(STATUS_START)
    val status: LiveData<Int> = _status

    private var _robot = Robot.getInstance()

    private var _ttsManager = TTSManager()

    init {
        resetExperience()
    }

    companion object {
        // STATUS
        const val STATUS_START = 0
        const val STATUS_VIDEO = 1
        const val STATUS_SPEAKING = 2
        const val STATUS_WALKING = 3
        const val STATUS_QR_CODE = 4
        // VIDEOS
        val VIDEO_PROMOTION: Uri = Uri.parse("android.resource://" + MainActivity.PACKAGE_NAME + "/" + R.raw.v1)
        val VIDEO_TEMI_FACE: Uri = Uri.parse("android.resource://" + MainActivity.PACKAGE_NAME + "/" + R.raw.v_temi)
    }

    fun setStatus(newStatus: Int) {
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

    fun initTTS(context: Context) {
        _ttsManager.init(context)
    }

    fun speakTTS(speech: String) {
        _ttsManager.initQueue(speech)
    }

    fun finishTTS() {
        _ttsManager.shutDown()
    }

    fun resetExperience() {
        _status.value = STATUS_START
    }
}