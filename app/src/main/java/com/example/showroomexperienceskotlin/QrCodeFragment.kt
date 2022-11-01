package com.example.showroomexperienceskotlin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.showroomexperienceskotlin.databinding.FragmentQrCodeBinding
import com.example.showroomexperienceskotlin.model.SharedViewModel

class QrCodeFragment : Fragment() {

    private var _binding: FragmentQrCodeBinding? = null
    private val binding get() = _binding!!
    // Shared ViewModel
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQrCodeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonNext2.setOnClickListener {
            sharedViewModel.resetExperience()
            findNavController().navigate(R.id.action_qrCodeFragment_to_startFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}