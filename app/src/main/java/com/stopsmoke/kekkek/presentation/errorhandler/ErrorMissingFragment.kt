package com.stopsmoke.kekkek.presentation.errorhandler

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.stopsmoke.kekkek.databinding.FragmentErrorMissingBinding
import com.stopsmoke.kekkek.invisible
import com.stopsmoke.kekkek.visible

class ErrorMissingFragment : Fragment() {

    private var _binding: FragmentErrorMissingBinding? = null
    private val binding: FragmentErrorMissingBinding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.invisible()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentErrorMissingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        activity?.visible()
        _binding = null
    }
}