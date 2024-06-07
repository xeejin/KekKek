package com.stopsmoke.kekkek.presentation.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.stopsmoke.kekkek.databinding.FragmentOnboardingFinishBinding
import com.stopsmoke.kekkek.presentation.DatastoreHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class OnboardingFinishFragment : Fragment() {

    private var _binding: FragmentOnboardingFinishBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOnboardingFinishBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            delay(6000)
            DatastoreHelper.setOnboardingComplete(requireContext(), true)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}