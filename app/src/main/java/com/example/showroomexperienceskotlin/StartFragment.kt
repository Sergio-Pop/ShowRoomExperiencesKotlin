package com.example.showroomexperienceskotlin

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.showroomexperienceskotlin.databinding.FragmentStartBinding
import com.example.showroomexperienceskotlin.model.SharedViewModel
import com.robotemi.sdk.listeners.OnDetectionStateChangedListener
import com.robotemi.sdk.listeners.OnGoToLocationStatusChangedListener
import kotlinx.coroutines.NonCancellable.start


class StartFragment : Fragment(), OnDetectionStateChangedListener, OnGoToLocationStatusChangedListener {
    // TAG
    private val TAG = "StartFragment"
    // View Binding
    private var _binding: FragmentStartBinding? = null
    private val binding get() = _binding!!
    // VideoView
    private lateinit var nMediaPlayer: MediaPlayer
    // Shared ViewModel
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Button
        binding.buttonNext.setOnClickListener {attendExternalEvent("Button")}

        // VideoView
        val uri: Uri = if (sharedViewModel.status.value=="Inicio")
            Uri.parse("android.resource://" + context?.packageName + "/" + R.raw.v1) else
            Uri.parse("android.resource://" + context?.packageName + "/" + R.raw.v_temi)
        binding.videoView.apply {
            setVideoURI(uri)
            start()
            setOnPreparedListener { mp ->
                nMediaPlayer = mp
                nMediaPlayer.isLooping = true
            }
        }

        // Robot
        sharedViewModel.initDetectionMode(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        sharedViewModel.finishDetectionMode(this)
        _binding = null
    }

    fun attendExternalEvent(actualStatus: String) {
        when(actualStatus){
            "Deteccion" -> {
                val uri2: Uri = Uri.parse("android.resource://" + context?.packageName + "/" + R.raw.v_temi)
                binding.videoView.setVideoURI(uri2)
                binding.videoView.start()
                sharedViewModel.initGoToLocationMode(this)
                sharedViewModel.goTo("1")

            }
            "Button"-> findNavController().navigate(R.id.action_startFragment_to_qrCodeFragment)
        }
        sharedViewModel.setStatus(actualStatus)
    }

    override fun onDetectionStateChanged(state: Int) {
        Log.i(TAG, "Temi ha detectado un cambio en el estado de deteccion")
        when (state) {
            0 -> Log.i(TAG, "No se ha detectado a ninguna persona")
            1 -> Log.i(TAG, "Perdí a la persona")
            2 -> {
                Log.i(TAG, "Detecte a alguien")
                attendExternalEvent("Deteccion")
            }
        }
    }

    override fun onGoToLocationStatusChanged(
        location: String,
        status: String,
        descriptionId: Int,
        description: String
    ) {
        Log.i(TAG, "Location: $location \n Status: $status \n DescriptionId: $descriptionId \n Description: $description \n")
        when (status) {
            OnGoToLocationStatusChangedListener.CALCULATING -> {
                sharedViewModel.setStatus("Walking")
            }
            OnGoToLocationStatusChangedListener.GOING -> {
                sharedViewModel.setStatus("Walking")
            }
            OnGoToLocationStatusChangedListener.COMPLETE -> {
                findNavController().navigate(R.id.action_startFragment_to_qrCodeFragment)
                sharedViewModel.setStatus("CodigoQR")
            }
        }
    }
}