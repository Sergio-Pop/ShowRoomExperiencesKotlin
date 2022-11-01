package com.example.showroomexperienceskotlin

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.showroomexperienceskotlin.databinding.FragmentStartBinding
import com.example.showroomexperienceskotlin.model.SharedViewModel


class StartFragment : Fragment() {

    // View Binding
    private var _binding: FragmentStartBinding? = null
    private val binding get() = _binding!!
    // VideoView
    private lateinit var nMediaPlayer: MediaPlayer
    // Counter click
    var i: Int = 0
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
        binding.buttonNext.setOnClickListener {attendExternalEvent()}

        // VideoView
        val uri1: Uri = Uri.parse("android.resource://" + context?.packageName + "/" + R.raw.v1)
        binding.videoView.apply {
            setVideoURI(uri1)
            start()
            setOnPreparedListener { mp ->
                nMediaPlayer = mp
                nMediaPlayer.isLooping = true
            }
        }

    }

    private fun attendExternalEvent() {
        sharedViewModel.status.value?.inc()?.let { sharedViewModel.setStatus(it) }
        when(sharedViewModel.status.value){
            1 -> {
                val uri2: Uri = Uri.parse("android.resource://" + context?.packageName + "/" + R.raw.v_temi)
                binding.videoView.setVideoURI(uri2)
                binding.videoView.start()
            }
            2-> findNavController().navigate(R.id.action_startFragment_to_qrCodeFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}