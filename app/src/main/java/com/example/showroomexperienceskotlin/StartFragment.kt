package com.example.showroomexperienceskotlin

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.VideoView
import androidx.compose.ui.input.key.Key.Companion.I
import androidx.navigation.fragment.findNavController



class StartFragment : Fragment() {

    private lateinit var videoView: VideoView
    private lateinit var nMediaPlayer: MediaPlayer
    //private val uri1: Uri = Uri.parse("android.resource://" + context?.packageName + "/" + R.raw.v1)
    //private val uri2: Uri = Uri.parse("android.resource://" + context?.packageName + "/" + R.raw.v_temi)
    var i: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_start, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Button
        val buttonNext: Button = view.findViewById(R.id.buttonNext)
        buttonNext.setOnClickListener {
            i++
            if(i==1) {
                val uri2: Uri = Uri.parse("android.resource://" + context?.packageName + "/" + R.raw.v_temi)
                videoView.setVideoURI(uri2)
                videoView.start()
            } else if(i==2) {
                i=0
                findNavController().navigate(R.id.action_startFragment_to_qrCodeFragment)
            }
        }

        // VideoView
        videoView = view.findViewById(R.id.videoView)
        val uri1: Uri = Uri.parse("android.resource://" + context?.packageName + "/" + R.raw.v1)
        val uri2: Uri = Uri.parse("android.resource://" + context?.packageName + "/" + R.raw.v_temi)
        videoView.setVideoURI(uri1)
        videoView.start()

        videoView.setOnPreparedListener { mp ->
            nMediaPlayer = mp
            nMediaPlayer.isLooping = true
        }
    }
}