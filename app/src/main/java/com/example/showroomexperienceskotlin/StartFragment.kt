package com.example.showroomexperienceskotlin

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.VideoView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.showroomexperienceskotlin.databinding.FragmentStartBinding
import com.example.showroomexperienceskotlin.model.SharedViewModel
import com.robotemi.sdk.listeners.OnDetectionStateChangedListener
import com.robotemi.sdk.listeners.OnGoToLocationStatusChangedListener


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

        // VideoView
        val uri: Uri = when (sharedViewModel.status.value) {
            SharedViewModel.STATUS_START -> SharedViewModel.VIDEO_PROMOTION
            SharedViewModel.STATUS_SPEAKING -> SharedViewModel.VIDEO_TEMI_FACE
            else -> SharedViewModel.VIDEO_PROMOTION
        }
        val videoView: VideoView = binding.videoView // Obtiene referencia de la vista VideoView de la IU
        initVideo(videoView, uri) // Inicializar video

        // Robot
        sharedViewModel.initDetectionMode(this) // Inicializar el modo deteccion
    }

    private fun initVideo(videoView: VideoView, videoUri: Uri) {
        videoView.apply {
            setVideoURI(videoUri)
            start()
            setOnPreparedListener() { mp ->
                nMediaPlayer = mp
                nMediaPlayer.isLooping = true
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        sharedViewModel.finishDetectionMode(this)
        _binding = null
    }

    private fun attendDetection() {
        val videoView: VideoView = binding.videoView
        sharedViewModel.initTTS(requireContext())
        sharedViewModel.speakTTS("Hola, sígueme y prueba el increíble sabor de la nueva Ades Avellana.")
        initVideo(videoView, SharedViewModel.VIDEO_TEMI_FACE)
        sharedViewModel.initGoToLocationMode(this)
        sharedViewModel.goTo("1")
        sharedViewModel.setStatus(SharedViewModel.STATUS_SPEAKING)
    }

    override fun onDetectionStateChanged(state: Int) {
        when (state) {
            0 -> Log.i(TAG, "No se ha detectado a ninguna persona")
            1 -> Log.i(TAG, "Perdí a la persona")
            2 -> {
                Log.i(TAG, "Detecte a alguien")
                attendDetection()
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
            OnGoToLocationStatusChangedListener.COMPLETE -> {
                if(sharedViewModel.status.value != SharedViewModel.STATUS_START) {
                    sharedViewModel.speakTTS("Llegamos! Disfruta del nuevo sabor de Ades Avellana.")
                    findNavController().navigate(R.id.action_startFragment_to_qrCodeFragment)
                    sharedViewModel.speakTTS("Si quieres seguir con la experiencia, escanea el siguiente código QR y juega con Ades Avellana. ")
                    sharedViewModel.setStatus(SharedViewModel.STATUS_QR_CODE)
                }
            }
        }
    }
}