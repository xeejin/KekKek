package com.stopsmoke.kekkek.presentation.userprofile.comment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.stopsmoke.kekkek.databinding.FragmentUserProfileCommentBinding
import com.stopsmoke.kekkek.presentation.userprofile.UserProfileViewModel
import com.stopsmoke.kekkek.presentation.userprofile.comment.adapter.UserProfileCommentListAdapter
import kotlinx.coroutines.launch

class UserProfileCommentFragment : Fragment() {

    private var _binding: FragmentUserProfileCommentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UserProfileViewModel by activityViewModels()

    private lateinit var commentListAdapter: UserProfileCommentListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentUserProfileCommentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        observeRecyclerViewItem()
    }

    private fun initRecyclerView() {
        commentListAdapter = UserProfileCommentListAdapter()
        binding.rvUserProfileRoot.adapter = commentListAdapter
        binding.root.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun observeRecyclerViewItem() = lifecycleScope.launch {
        viewModel.myCommentHistory.collect {
            commentListAdapter.submitData(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}