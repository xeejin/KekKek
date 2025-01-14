package com.stopsmoke.kekkek.presentation.my.achievement

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.stopsmoke.kekkek.common.Result
import com.stopsmoke.kekkek.core.domain.model.User
import com.stopsmoke.kekkek.databinding.FragmentAchievementBinding
import com.stopsmoke.kekkek.presentation.collectLatestWithLifecycle
import com.stopsmoke.kekkek.presentation.error.ErrorHandle
import com.stopsmoke.kekkek.presentation.invisible
import com.stopsmoke.kekkek.presentation.my.MyViewModel
import com.stopsmoke.kekkek.presentation.my.achievement.adapter.AchievementListAdapter
import com.stopsmoke.kekkek.presentation.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AchievementFragment : Fragment(), ErrorHandle {

    private var _binding: FragmentAchievementBinding? = null
    private val binding: FragmentAchievementBinding get() = _binding!!

    private val achievementListAdapter: AchievementListAdapter by lazy {
        AchievementListAdapter()
    }

    private val viewModel: MyViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAchievementBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initViewModel()
    }


    private fun initView() = with(binding) {
        setupAppBar()
        setupRecyclerView()
    }

    private fun setupRecyclerView() = with(binding.rvAchievementItem) {
        adapter = achievementListAdapter
        layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setupAppBar() = with(binding.includeAchievementAppBar) {
        ivAchievementBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initViewModel() = with(viewModel) {
        activities.collectLatestWithLifecycle(lifecycle) {
            when (it) {
                is Result.Success -> {
                    viewModel.getCurrentProgress()
                    bindTopProgress()
                }

                is Result.Error -> {
                    it.exception?.printStackTrace()
                    errorExit(findNavController())
                }
                Result.Loading -> {}
            }
        }

        achievements.collectLatestWithLifecycle(lifecycle) {
            achievementListAdapter.submitList(it)
        }
    }

    private suspend fun bindTopProgress() = with(binding) {
        val progress = viewModel.getCurrentItem()
        val user = viewModel.user.value as User.Registered
        val maxProgressCount = viewModel.getAchievementCount()
        icludeAchievementTop.tvAchievementQuitSmokingDayCount.text = "${progress.user} 일"
        icludeAchievementTop.tvAchievementQuitSmokingCount.text =
            "${user.clearAchievementsList.size} / ${maxProgressCount}"
    }


    override fun onResume() {
        super.onResume()
        activity?.invisible()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        activity?.visible()
    }
}

